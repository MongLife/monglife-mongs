package com.mongs.play.app.battle.worker.event;

import com.mongs.play.app.battle.worker.service.BattleWorkerService;
import com.mongs.play.client.publisher.battle.event.MatchExitEvent;
import com.mongs.play.client.publisher.battle.event.MatchPickEvent;
import com.mongs.play.client.publisher.battle.event.MatchWaitEvent;
import com.mongs.play.client.publisher.battle.event.MatchEnterEvent;
import com.mongs.play.domain.battle.code.PickCode;
import com.mongs.play.domain.battle.vo.BattleRoomVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleWorkerEventListener {

    private final BattleWorkerService battleWorkerService;

    @Async
    @EventListener
    public void matchWait(MatchWaitEvent event) {
        Long mongId = event.mongId();
        String deviceId = event.deviceId();
        battleWorkerService.matchWait(deviceId, mongId);
    }

    @Async
    @EventListener
    public void matchEnter(MatchEnterEvent event) {
        String roomId = event.roomId();
        String playerId = event.playerId();
        battleWorkerService.matchEnter(roomId, playerId);
    }

    @Async
    @EventListener
    public void matchPick(MatchPickEvent event) {
        String roomId = event.roomId();
        String playerId = event.playerId();
        String targetPlayerId = event.targetPlayerId();
        PickCode pick = PickCode.valueOf(event.pick());
        BattleRoomVo battleRoomVo = battleWorkerService.matchPick(roomId, playerId, targetPlayerId, pick);
        battleWorkerService.matchOver(battleRoomVo);
    }

    @Async
    @EventListener
    public void matchExit(MatchExitEvent event) {
        String roomId = event.roomId();
        String playerId = event.playerId();
        battleWorkerService.matchExit(roomId, playerId);
    }
}
