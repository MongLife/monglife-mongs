package com.mongs.play.app.battle.worker.service;

import com.mongs.play.app.battle.worker.code.BattleState;
import com.mongs.play.app.battle.worker.utils.MatchUtil;
import com.mongs.play.app.battle.worker.vo.RoundResultVo;
import com.mongs.play.client.publisher.battle.service.MqttService;
import com.mongs.play.client.publisher.battle.vo.res.MatchFindVo;
import com.mongs.play.client.publisher.battle.vo.res.MatchOverVo;
import com.mongs.play.client.publisher.battle.vo.res.MatchPlayerVo;
import com.mongs.play.client.publisher.battle.vo.res.MatchVo;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.battle.code.PickCode;
import com.mongs.play.domain.battle.service.BattleService;
import com.mongs.play.domain.battle.vo.BattlePlayerVo;
import com.mongs.play.domain.battle.vo.BattleRoomVo;
import com.mongs.play.domain.battle.vo.BattleRoundVo;
import com.mongs.play.domain.match.service.MatchService;
import com.mongs.play.domain.match.vo.MatchWaitVo;
import com.mongs.play.domain.mong.service.MongPayPointService;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.service.MongStatusService;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleWorkerService {

    private final Random random = new Random();
    private final MongService mongService;
    private final MongPayPointService mongPayPointService;
    private final MatchService matchService;
    private final BattleService battleService;
    private final MqttService mqttService;

    private final Integer MAX_PLAYER = 2;
    private final Integer MAX_ROUND = 10;
    private final Integer BATTLE_REWARD_PAY_POINT = 100;

    public Set<MatchWaitVo> matchSearch() {
        return matchService.findMatch();
    }

    public void matchWait(String deviceId, Long mongId) {
        matchService.addMatch(deviceId, mongId);
    }

    @Transactional
    public void botMatchFind(MatchWaitVo matchWaitVo) {
        BattleRoomVo battleRoomVo = battleService.addBattleRoom();

        MongVo mongVo = mongService.findActiveMongById(matchWaitVo.mongId());

        double attackValue = mongVo.strength();
        double healValue = mongVo.sleep();

        BattlePlayerVo battlePlayerVo = battleService.addBattlePlayer(matchWaitVo.playerId(), mongVo.mongId(), mongVo.mongCode(), attackValue, healValue, false);

        // 봇 생성
        String botPlayerId = UUID.randomUUID().toString().replace("-", "");
        BattlePlayerVo botBattlePlayerVo = battleService.addBattlePlayer(botPlayerId, -1L, "CH100", 100D, 50D, true);
        battleRoomVo = battleService.enterBattleRoom(battleRoomVo.roomId(), botBattlePlayerVo.playerId());

        for (int nowRound = 0; nowRound < MAX_ROUND; nowRound++) {
            PickCode pick = PickCode.values()[random.nextInt(PickCode.values().length)];
            if (pick.equals(PickCode.ATTACK)) {
                battleService.addBattleRound(battleRoomVo.roomId(), botBattlePlayerVo.playerId(), battlePlayerVo.playerId(), pick);
            } else {
                battleService.addBattleRound(battleRoomVo.roomId(), botBattlePlayerVo.playerId(), botBattlePlayerVo.playerId(), pick);
            }
        }

        mqttService.sendMatchFind(matchWaitVo.deviceId(), MatchFindVo.builder()
                .roomId(battleRoomVo.roomId())
                .playerId(battlePlayerVo.playerId())
                .build());

        log.info("[match] [{}, {}]", battlePlayerVo.playerId(), botBattlePlayerVo.playerId());
    }

    @Transactional
    public void matchFind(Set<MatchWaitVo> matchWaitVoSet) {
        BattleRoomVo battleRoomVo = battleService.addBattleRoom();

        matchWaitVoSet.forEach(matchWaitVo -> {
            MongVo mongVo = mongService.findActiveMongById(matchWaitVo.mongId());

            double attackValue = mongVo.strength();
            double healValue = mongVo.sleep();

            BattlePlayerVo battlePlayerVo = battleService.addBattlePlayer(matchWaitVo.playerId(), mongVo.mongId(), mongVo.mongCode(), attackValue, healValue, false);

            mqttService.sendMatchFind(matchWaitVo.deviceId(), MatchFindVo.builder()
                    .roomId(battleRoomVo.roomId())
                    .playerId(battlePlayerVo.playerId())
                    .build());
        });

        log.info("[match] {}", matchWaitVoSet);
    }

    @Transactional
    public void matchEnter(String roomId, String playerId) {
        BattleRoomVo battleRoomVo = battleService.enterBattleRoom(roomId, playerId);

        log.info("[match] {} room enter player : {}", roomId, playerId);

        if (battleRoomVo.battlePlayerVoList().size() >= MAX_PLAYER) {
            mqttService.sendMatch(battleRoomVo.roomId(), MatchVo.builder()
                    .roomId(battleRoomVo.roomId())
                    .round(battleRoomVo.round())
                    .matchPlayerVoList(battleRoomVo.battlePlayerVoList().stream()
                            .map(battlePlayerVo -> MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.NONE.name())
                                    .build())
                            .toList())
                    .build());

            battleService.increaseRound(battleRoomVo.roomId());

            log.info("[match] {} room battle start", roomId);
        }
    }

    @Transactional
    public BattleRoomVo matchPick(String roomId, String playerId, String targetPlayerId, PickCode pick) {
        BattleRoomVo battleRoomVo = battleService.addBattleRound(roomId, playerId, targetPlayerId, pick);

        log.info("[match] {} room player pick : {} to {}", roomId, pick, playerId);

        return battleRoomVo;
    }

    @Transactional
    public void matchExit(String roomId, String playerId) {
        BattleRoomVo battleRoomVo = battleService.exitBattleRoom(roomId, playerId);

        log.info("[match] {} room exit player : {}", roomId, playerId);

        if (battleRoomVo.battlePlayerVoList().size() == 1) {

            BattlePlayerVo winPlayerVo = battleRoomVo.battlePlayerVoList().get(0);

            mqttService.sendMatchOver(battleRoomVo.roomId(), MatchOverVo.builder()
                    .roomId(battleRoomVo.roomId())
                    .winPlayer(MatchPlayerVo.builder()
                                .playerId(winPlayerVo.playerId())
                                .mongCode(winPlayerVo.mongCode())
                                .build())
                    .build());

            battleService.removeBattle(roomId);

            mongPayPointService.increasePayPoint(winPlayerVo.mongId(), BATTLE_REWARD_PAY_POINT);

            log.info("[match] {} room battle stop, {} player win", roomId, battleRoomVo.battlePlayerVoList().get(0).playerId());
        }
    }

    @Transactional
    public void matchOver(BattleRoomVo battleRoomVo) {
        int nowRound = battleRoomVo.round();
        List<String> playerIdList = battleRoomVo.battlePlayerVoList().stream()
                .map(BattlePlayerVo::playerId)
                .toList();
        List<String> roundPlayerIdList = battleRoomVo.battleRoundVoList().stream()
                .filter(battleRoundVo -> battleRoundVo.round() == nowRound)
                .map(BattleRoundVo::playerId)
                .toList();

        if (MatchUtil.validateMatchPick(playerIdList, roundPlayerIdList)) {
            List<MatchPlayerVo> matchPlayerVoList = MatchUtil.setRoundResult(battleRoomVo).values().stream()
                    .map(roundResultVo -> {
                        if (roundResultVo.isDefenced() && roundResultVo.isAttacked() && roundResultVo.isHealed()) {
                            // 회복
                            BattlePlayerVo battlePlayerVo = battleService.healPlayer(roundResultVo.getPlayerId());
                            return MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.HEAL.name())
                                    .build();
                        } else if (roundResultVo.isDefenced() && roundResultVo.isAttacked()) {
                            // 방어
                            BattlePlayerVo battlePlayerVo = battleService.getBattlePlayer(roundResultVo.getPlayerId());
                            return MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.DEFENCE.name())
                                    .build();
                        } else if (roundResultVo.isAttacked() && roundResultVo.isHealed()) {
                            // 공격 & 회복
                            BattlePlayerVo battlePlayerVo = battleService.attackWithHealPlayer(roundResultVo.getPlayerId(), roundResultVo.getDamage());
                            return MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.DAMAGE_AND_HEAL.name())
                                    .build();
                        } else if (roundResultVo.isDefenced() && roundResultVo.isHealed()) {
                            // 회복
                            BattlePlayerVo battlePlayerVo = battleService.healPlayer(roundResultVo.getPlayerId());
                            return MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.HEAL.name())
                                    .build();
                        } else if (roundResultVo.isDefenced()) {
                            // 방어
                            BattlePlayerVo battlePlayerVo = battleService.getBattlePlayer(roundResultVo.getPlayerId());
                            return MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.DEFENCE.name())
                                    .build();
                        } else if (roundResultVo.isAttacked()) {
                            // 공격
                            BattlePlayerVo battlePlayerVo = battleService.attackPlayer(roundResultVo.getPlayerId(), roundResultVo.getDamage());
                            return MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.DAMAGE.name())
                                    .build();
                        } else if (roundResultVo.isHealed()) {
                            // 회복
                            BattlePlayerVo battlePlayerVo = battleService.healPlayer(roundResultVo.getPlayerId());
                            return MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.HEAL.name())
                                    .build();
                        } else {
                            BattlePlayerVo battlePlayerVo = battleService.getBattlePlayer(roundResultVo.getPlayerId());
                            return MatchPlayerVo.builder()
                                    .playerId(battlePlayerVo.playerId())
                                    .mongCode(battlePlayerVo.mongCode())
                                    .hp(battlePlayerVo.hp())
                                    .state(BattleState.NONE.name())
                                    .build();
                        }
                    }).toList();

            mqttService.sendMatch(battleRoomVo.roomId(), MatchVo.builder()
                    .roomId(battleRoomVo.roomId())
                    .round(battleRoomVo.round())
                    .matchPlayerVoList(matchPlayerVoList)
                    .build());

            battleRoomVo = battleService.getBattleRoom(battleRoomVo.roomId());

            // 마지막 라운드 또는 꼴등이 가려졌을 때 매치 종료
            if (battleRoomVo.round().equals(MAX_ROUND) || MatchUtil.validateMatchPlayerZeroHp(battleRoomVo.battlePlayerVoList())) {
                BattlePlayerVo winPlayerVo = battleRoomVo.battlePlayerVoList().get(0);
                for (BattlePlayerVo battlePlayerVo : battleRoomVo.battlePlayerVoList()) {
                    if (battlePlayerVo.hp() > winPlayerVo.hp()) {
                        winPlayerVo = battlePlayerVo;
                    }
                }

                mqttService.sendMatchOver(battleRoomVo.roomId(), MatchOverVo.builder()
                        .roomId(battleRoomVo.roomId())
                        .winPlayer(MatchPlayerVo.builder()
                                .playerId(winPlayerVo.playerId())
                                .mongCode(winPlayerVo.mongCode())
                                .build())
                        .build());

                battleService.removeBattle(battleRoomVo.roomId());

                mongPayPointService.increasePayPoint(winPlayerVo.mongId(), BATTLE_REWARD_PAY_POINT);

            } else {
                battleService.increaseRound(battleRoomVo.roomId());
            }

            log.info("[match] {} room battle round over", battleRoomVo.roomId());
        }
    }
}
