package com.monglife.mongs.app.activity.battle.schdeuler;

import com.monglife.mongs.app.activity.battle.controller.BattleConsumer;
import com.monglife.mongs.app.activity.battle.dto.request.CreateBattleRequestDto;
import com.monglife.mongs.app.activity.battle.vo.CreateBattleVo;
import com.monglife.mongs.domain.match.service.MatchingService;
import com.monglife.mongs.domain.match.vo.FindMatchingVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleMatchingScheduler {

    private final MatchingService matchingService;

    private final BattleConsumer battleConsumer;


    @Scheduled(fixedDelay = 1000)
    public void matchingSchedule() {

        while (true) {
            Set<FindMatchingVo> findMatchingVoSet = matchingService.findWaitMatching();

            if (findMatchingVoSet.isEmpty()) break;

            Set<CreateBattleVo> createBattleVoSet = findMatchingVoSet.stream()
                    .map(findMatchingVo -> CreateBattleVo.builder()
                            .deviceId(findMatchingVo.getDeviceId())
                            .accountId(findMatchingVo.getAccountId())
                            .mongId(findMatchingVo.getMongId())
                            .isBot(findMatchingVo.getIsBot())
                            .build())
                    .collect(Collectors.toSet());

            battleConsumer.createBattle(CreateBattleRequestDto.builder()
                    .createBattleVoSet(createBattleVoSet)
                    .build());
        }
    }
}
