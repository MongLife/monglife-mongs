package com.mongs.play.app.battle.worker.service;

import com.mongs.play.app.battle.worker.code.BattleState;
import com.mongs.play.app.battle.worker.utils.MatchUtil;
import com.mongs.play.client.publisher.battle.service.MqttBattleService;
import com.mongs.play.client.publisher.battle.vo.res.MatchOverVo;
import com.mongs.play.client.publisher.battle.vo.res.MatchPlayerVo;
import com.mongs.play.client.publisher.battle.vo.res.MatchVo;
import com.mongs.play.domain.battle.code.PickCode;
import com.mongs.play.domain.battle.service.BattleService;
import com.mongs.play.domain.battle.vo.BattlePlayerVo;
import com.mongs.play.domain.battle.vo.BattleRoomVo;
import com.mongs.play.domain.battle.vo.BattleRoundVo;
import com.mongs.play.domain.mong.service.MongPayPointService;
import com.mongs.play.domain.mong.service.MongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BattleMatchService {

    private final MongPayPointService mongPayPointService;
    private final MongService mongService;
    private final BattleService battleService;
    private final MqttBattleService mqttBattleService;

    private final static Integer MAX_PLAYER = 2;
    private final static Integer MAX_ROUND = 10;
    private final static Integer BATTLE_REWARD_PAY_POINT = 100;

    @Transactional
    public BattleRoomVo findMatch(String roomId) {
        return battleService.getBattleRoom(roomId);
    }

    @Transactional
    public void matchEnter(String roomId, String playerId) {
        BattleRoomVo battleRoomVo = battleService.enterBattleRoom(roomId, playerId);

        if (battleRoomVo.battlePlayerVoList().size() >= MAX_PLAYER) {
            mqttBattleService.sendMatch(battleRoomVo.roomId(), MatchVo.builder()
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

            battleService.increaseRound(roomId);
        }
    }

    @Transactional
    public BattleRoomVo matchPick(String roomId, String playerId, String targetPlayerId, PickCode pick) {
        return battleService.addBattleRound(roomId, playerId, targetPlayerId, pick);
    }

    @Transactional
    public void matchExit(String roomId, String playerId) {
        BattleRoomVo battleRoomVo = battleService.exitBattleRoom(roomId, playerId);

        if (battleRoomVo.battlePlayerVoList().size() == 1) {
            BattlePlayerVo winPlayerVo = battleRoomVo.battlePlayerVoList().get(0);

            mqttBattleService.sendMatchOver(battleRoomVo.roomId(), MatchOverVo.builder()
                        .roomId(battleRoomVo.roomId())
                        .winPlayer(MatchPlayerVo.builder()
                                    .playerId(winPlayerVo.playerId())
                                    .mongCode(winPlayerVo.mongCode())
                                    .build())
                        .build());

            battleService.removeBattle(roomId);

            if (!winPlayerVo.isBot()) {
                mongPayPointService.increasePayPoint(winPlayerVo.mongId(), BATTLE_REWARD_PAY_POINT);
                mongService.increaseStatusBattle(winPlayerVo.mongId());
            }
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

            battleRoomVo = battleService.getBattleRoom(battleRoomVo.roomId());

            // 마지막 라운드 또는 꼴등이 가려졌을 때 매치 종료
            if (battleRoomVo.round().equals(MAX_ROUND) || MatchUtil.validateMatchPlayerZeroHp(battleRoomVo.battlePlayerVoList())) {
                BattlePlayerVo winPlayerVo = battleRoomVo.battlePlayerVoList().get(0);
                for (BattlePlayerVo battlePlayerVo : battleRoomVo.battlePlayerVoList()) {
                    if (battlePlayerVo.hp() > winPlayerVo.hp()) {
                        winPlayerVo = battlePlayerVo;
                    }
                    if (!battlePlayerVo.isBot()) {
                        mongService.increaseStatusBattle(battlePlayerVo.mongId());
                    }
                }

                mqttBattleService.sendMatchOver(battleRoomVo.roomId(), MatchOverVo.builder()
                        .roomId(battleRoomVo.roomId())
                        .round(battleRoomVo.round())
                        .matchPlayerVoList(matchPlayerVoList)
                        .winPlayer(MatchPlayerVo.builder()
                                .playerId(winPlayerVo.playerId())
                                .mongCode(winPlayerVo.mongCode())
                                .build())
                        .build());

                battleService.removeBattle(battleRoomVo.roomId());

                if (!winPlayerVo.isBot()) {
                    mongPayPointService.increasePayPoint(winPlayerVo.mongId(), BATTLE_REWARD_PAY_POINT);
                }

            } else {
                mqttBattleService.sendMatch(battleRoomVo.roomId(), MatchVo.builder()
                        .roomId(battleRoomVo.roomId())
                        .round(battleRoomVo.round())
                        .matchPlayerVoList(matchPlayerVoList)
                        .build());

                battleService.increaseRound(battleRoomVo.roomId());
            }
        }
    }
}
