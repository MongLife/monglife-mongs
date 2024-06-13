package com.mongs.play.app.battle.worker.consumer;

import com.mongs.play.app.battle.worker.service.MatchSearchService;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.match.vo.MatchVo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class MatchSearchScheduler {
    private final MatchSearchService matchSearchService;

    @Scheduled(fixedDelay = 1000)
    public void matchSearch() {
        while (true) {
            try {
                Set<MatchVo> matchVoSet = matchSearchService.matchSearch();
                matchSearchService.matchFind(matchVoSet.stream().map(MatchVo::mongId).toList());
            } catch (NotFoundException e) {
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
