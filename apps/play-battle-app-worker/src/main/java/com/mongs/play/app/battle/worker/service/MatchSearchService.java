package com.mongs.play.app.battle.worker.service;

import com.mongs.play.client.publisher.battle.service.MqttService;
import com.mongs.play.client.publisher.battle.vo.res.MatchFindVo;
import com.mongs.play.client.publisher.battle.vo.res.MatchVo;
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
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchSearchService {

    private final MongService mongService;
    private final MatchService matchService;
    private final BattleService battleService;
    private final MqttService mqttService;

    public Set<MatchWaitVo> matchSearch() {
        return matchService.findMatch();
    }

    public void matchWait(String deviceId, Long mongId) {
        matchService.addMatch(deviceId, mongId);
    }

    @Transactional
    public void matchFind(Set<MatchWaitVo> matchWaitVoSet) {

        BattleRoomVo battleRoomVo = battleService.addBattleRoom();

        matchWaitVoSet.forEach(matchWaitVo -> {
            MongVo mongVo = mongService.findActiveMongById(matchWaitVo.mongId());

            double attackValue = mongVo.strength();
            double healValue = mongVo.sleep();

            battleService.addBattlePlayer(BattlePlayerVo.builder()
                    .playerId(matchWaitVo.playerId())
                    .mongId(mongVo.mongId())
                    .mongCode(mongVo.mongCode())
                    .attackValue(attackValue)
                    .healValue(healValue)
                    .build());

            mqttService.sendMatchFind(matchWaitVo.deviceId(), MatchFindVo.builder()
                    .roomId(battleRoomVo.roomId())
                    .playerId(matchWaitVo.playerId())
                    .build());
        });

        log.info("[match] {}", matchWaitVoSet);
    }

    @Transactional
    public void matchEnter(String roomId, String playerId) {

        BattleRoomVo battleRoomVo = battleService.enterBattleRoom(roomId, playerId);

        log.info("[match] {} room enter player : {}", roomId, playerId);

        if (battleRoomVo.battlePlayerVoList().size() >= 2) {
            // TODO ("배틀 시작 알림")
            mqttService.sendRound(battleRoomVo.roomId(), MatchVo.builder()
                    .build());

            log.info("[match] {} room battle start", roomId);
        }
    }

    @Transactional
    public void matchExit(String roomId, String playerId) {

        BattleRoomVo battleRoomVo = battleService.exitBattleRoom(roomId, playerId);

        log.info("[match] {} room exit player : {}", roomId, playerId);

        if (battleRoomVo.battlePlayerVoList().size() == 1) {
            // TODO ("배틀 종료 알림")
            mqttService.sendRound(battleRoomVo.roomId(), MatchVo.builder()
                    .build());

            log.info("[match] {} room battle stop, {} player win", roomId, battleRoomVo.battlePlayerVoList().get(0).playerId());
        }
    }
}
