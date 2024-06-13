package com.mongs.play.app.battle.worker.consumer;

import com.mongs.play.app.battle.worker.service.MatchSearchService;
import com.mongs.play.client.publisher.battle.vo.MatchWaitVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleWorkerConsumer {

    private final MatchSearchService matchSearchService;

    @Async
    @EventListener
    public void matchWait(MatchWaitVo event) {
        log.info("{}", event);
        Long mongId = event.mongId();
        matchSearchService.matchWait(mongId);
    }
}
