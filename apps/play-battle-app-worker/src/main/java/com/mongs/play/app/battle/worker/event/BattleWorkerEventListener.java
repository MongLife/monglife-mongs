package com.mongs.play.app.battle.worker.event;

import com.mongs.play.app.battle.worker.service.MatchSearchService;
import com.mongs.play.client.publisher.battle.event.MatchExitEvent;
import com.mongs.play.client.publisher.battle.event.MatchPickEvent;
import com.mongs.play.client.publisher.battle.event.MatchWaitEvent;
import com.mongs.play.client.publisher.battle.event.MatchEnterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleWorkerEventListener {

    private final MatchSearchService matchSearchService;

    @Async
    @EventListener
    public void matchWait(MatchWaitEvent event) {
        Long mongId = event.mongId();
        String deviceId = event.deviceId();
        matchSearchService.matchWait(deviceId, mongId);
    }

    @Async
    @EventListener
    public void matchEnter(MatchEnterEvent event) {
        String roomId = event.roomId();
        String playerId = event.playerId();
        matchSearchService.matchEnter(roomId, playerId);
    }

    @Async
    @EventListener
    public void matchPick(MatchPickEvent event) {
    }

    @Async
    @EventListener
    public void matchExit(MatchExitEvent event) {
        String roomId = event.roomId();
        String playerId = event.playerId();
        matchSearchService.matchExit(roomId, playerId);
    }
}
