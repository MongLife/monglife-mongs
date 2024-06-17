package com.mongs.play.app.battle.worker.event;

import com.mongs.play.app.battle.worker.service.BattleMatchService;
import com.mongs.play.client.publisher.battle.event.MatchExitEvent;
import com.mongs.play.client.publisher.battle.event.MatchPickEvent;
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

    private final BattleMatchService battleMatchService;

    @Async
    @EventListener
    public void matchEnter(MatchEnterEvent event) {
        String roomId = event.roomId();
        String playerId = event.playerId();
        battleMatchService.matchEnter(roomId, playerId);
    }

    @Async
    @EventListener
    public void matchPick(MatchPickEvent event) {
        String roomId = event.roomId();
        String playerId = event.playerId();
        String targetPlayerId = event.targetPlayerId();
        PickCode pick = PickCode.valueOf(event.pick());
        BattleRoomVo battleRoomVo = battleMatchService.matchPick(roomId, playerId, targetPlayerId, pick);
        battleMatchService.matchOver(battleRoomVo);
    }

    @Async
    @EventListener
    public void matchExit(MatchExitEvent event) {
        String roomId = event.roomId();
        String playerId = event.playerId();
        battleMatchService.matchExit(roomId, playerId);
    }
}
