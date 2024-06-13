package com.mongs.play.app.battle.worker.consumer;

import com.mongs.play.app.battle.worker.service.MatchSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/battle")
public class BattleWorkerConsumer {

    private final MatchSearchService matchSearchService;

    @GetMapping("/queue/{mongId}")
    public void matchSearch(@PathVariable Long mongId) {
        matchSearchService.matchWait(mongId);
    }
}
