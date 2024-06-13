package com.mongs.play.app.battle.worker.service;

import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.battle.service.BattleService;
import com.mongs.play.domain.battle.vo.BattlePlayerVo;
import com.mongs.play.domain.match.service.MatchService;
import com.mongs.play.domain.match.vo.MatchVo;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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

    public void matchWait(Long mongId) {
        matchService.addMatch(mongId);
    }

    public Set<MatchVo> matchSearch() {
        return matchService.getMatch();
    }

    @Async
    @Transactional
    public void matchFind(List<Long> mongIdList) {
        log.info("[match] {}", mongIdList);

        List<BattlePlayerVo> battlePlayerVoList = mongIdList.stream()
                        .map(mongId -> {
                            MongVo mongVo = mongService.findActiveMongById(mongId);

                            double attackValue = mongVo.strength();
                            double healValue = mongVo.sleep();

                            return BattlePlayerVo.builder()
                                    .mongId(mongVo.mongId())
                                    .mongCode(mongVo.mongCode())
                                    .attackValue(attackValue)
                                    .healValue(healValue)
                                    .build();
                        }).toList();

        battleService.addBattleRoom(battlePlayerVoList);
    }
}
