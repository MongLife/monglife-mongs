package com.mongs.play.app.battle.worker.service;

import com.mongs.play.app.battle.worker.vo.RegisterMatchWaitVo;
import com.mongs.play.app.battle.worker.vo.RemoveMatchWaitVo;
import com.mongs.play.client.publisher.battle.service.MqttBattleService;
import com.mongs.play.client.publisher.battle.vo.res.MatchFindVo;
import com.mongs.play.domain.battle.code.PickCode;
import com.mongs.play.domain.battle.service.BattleService;
import com.mongs.play.domain.battle.vo.BattlePlayerVo;
import com.mongs.play.domain.battle.vo.BattleRoomVo;
import com.mongs.play.domain.match.service.MatchService;
import com.mongs.play.domain.match.vo.MatchWaitVo;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleSearchService {

    private final Random random = new Random();
    private final MongService mongService;
    private final MatchService matchService;
    private final BattleService battleService;
    private final MqttBattleService mqttBattleService;

    private final static Integer MAX_ROUND = 10;
    private final static List<String> BOT_MONG_CODE_LIST = List.of("CH100", "CH101", "CH102", "CH210", "CH211", "CH212");
    private final static Double DEFAULT_ATTACK_VALUE = 50D;
    private final static Double DEFAULT_HEAL_VALUE = 30D;
    private final static Double DEFAULT_DEFENCE_VALUE = 10D;


    @Transactional
    public Set<MatchWaitVo> matchSearch() {
        return matchService.findMatch();
    }

    @Transactional
    public RegisterMatchWaitVo registerMatchWait(String deviceId, Long mongId) {
        MatchWaitVo matchWaitVo = matchService.addMatch(deviceId, mongId);

        return RegisterMatchWaitVo.builder()
                .mongId(matchWaitVo.mongId())
                .deviceId(matchWaitVo.deviceId())
                .build();
    }

    @Transactional
    public RemoveMatchWaitVo removeMatchWait(String deviceId, Long mongId) {
        MatchWaitVo matchWaitVo = matchService.deleteMatch(deviceId, mongId);

        return RemoveMatchWaitVo.builder()
                .mongId(matchWaitVo.mongId())
                .deviceId(matchWaitVo.deviceId())
                .build();
    }

    @Transactional
    public void matchFind(Set<MatchWaitVo> matchWaitVoSet) {
        BattleRoomVo battleRoomVo = battleService.addBattleRoom();

        Set<String> playerIdSet = matchWaitVoSet.stream()
                        .filter(matchWaitVo -> !matchWaitVo.isBot())
                        .map(MatchWaitVo::playerId)
                        .collect(Collectors.toSet());

        matchWaitVoSet.forEach(matchWaitVo -> {
            if (!matchWaitVo.isBot()) {
                // 유저인 경우
                MongVo mongVo = mongService.findActiveMongById(matchWaitVo.mongId());

                double attackValue = DEFAULT_ATTACK_VALUE + (mongVo.strength() / mongVo.grade().maxStatus * 100D);
                double healValue = DEFAULT_HEAL_VALUE + (mongVo.sleep() / mongVo.grade().maxStatus * 100D);
                double defenceValue = DEFAULT_DEFENCE_VALUE + (mongVo.weight() / mongVo.grade().maxStatus * 100D);

                attackValue = Math.max(0, attackValue);
                healValue = Math.max(0, healValue);
                defenceValue = Math.max(0, defenceValue);

                BattlePlayerVo battlePlayerVo = battleService.addBattlePlayer(matchWaitVo.playerId(), mongVo.mongId(), mongVo.mongCode(), attackValue, healValue, defenceValue, false);

                mqttBattleService.sendMatchFind(matchWaitVo.deviceId(), MatchFindVo.builder()
                        .roomId(battleRoomVo.roomId())
                        .playerId(battlePlayerVo.playerId())
                        .build());
            } else {
                // 봇인 경우
                double attackValue = DEFAULT_ATTACK_VALUE + random.nextDouble(-10, 10);
                double healValue = DEFAULT_HEAL_VALUE + random.nextDouble(-10, 10);
                double defenceValue = DEFAULT_DEFENCE_VALUE + random.nextDouble(-10, 10);

                attackValue = Math.max(0, attackValue);
                healValue = Math.max(0, healValue);
                defenceValue = Math.max(0, defenceValue);

                int mongCodeIndex = random.nextInt(BOT_MONG_CODE_LIST.size());
                BattlePlayerVo battlePlayerVo = battleService.addBattlePlayer(matchWaitVo.playerId(), matchWaitVo.mongId(), BOT_MONG_CODE_LIST.get(mongCodeIndex), attackValue, healValue, defenceValue, true);
                battleService.enterBattleRoom(battleRoomVo.roomId(), battlePlayerVo.playerId());

                // 랜덤 라운드 생성
                for (int nowRound = 1; nowRound <= MAX_ROUND; nowRound++) {
                    PickCode pick = PickCode.values()[random.nextInt(PickCode.values().length)];
                    if (pick.equals(PickCode.ATTACK)) {
                        String randomPlayerId = playerIdSet.stream().findAny().orElseGet(battlePlayerVo::playerId);
                        battleService.addBattleRound(battleRoomVo.roomId(), battlePlayerVo.playerId(), randomPlayerId, pick, nowRound);
                    } else {
                        battleService.addBattleRound(battleRoomVo.roomId(), battlePlayerVo.playerId(), battlePlayerVo.playerId(), pick, nowRound);
                    }
                }
            }
        });
    }
}
