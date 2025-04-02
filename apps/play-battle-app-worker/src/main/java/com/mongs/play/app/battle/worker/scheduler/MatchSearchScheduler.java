package com.mongs.play.app.battle.worker.scheduler;

import com.mongs.play.app.battle.worker.service.BattleSearchService;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.match.vo.MatchWaitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class MatchSearchScheduler {

    private final BattleSearchService battleSearchService;

    @Scheduled(fixedDelay = 1000)
    public void matchSearch() {
        while (true) {
            try {
                Set<MatchWaitVo> matchWaitVoSet = battleSearchService.matchSearch();
                battleSearchService.matchFind(matchWaitVoSet);
            } catch (NotFoundException e) {
                break;
            }
        }
    }
}
