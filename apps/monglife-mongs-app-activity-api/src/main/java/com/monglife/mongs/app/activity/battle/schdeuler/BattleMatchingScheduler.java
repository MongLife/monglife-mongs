package com.monglife.mongs.app.activity.battle.schdeuler;

import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.publisher.BattlePublisher;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.domain.match.service.MatchingService;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.domain.match.vo.MatchingVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleMatchingScheduler {

    private final MatchingService matchingService;

    private final BattleService battleService;

    private final BattlePublisher battlePublisher;

    /**
     * 배틀 매칭 스케줄러
     */
    @Scheduled(fixedDelay = 1000)
    public void matchingSchedule() {

        while (true) {
            Set<MatchingVo> matchingVoSet = matchingService.getWaitMatching();

            if (matchingVoSet.isEmpty()) break;

            CreateBattleDto createBattleDto = battleService.createBattle(matchingVoSet);

            List<String> deviceIds = createBattleDto.getMatchPlayers().stream()
                    .map(MatchPlayerVo::getDeviceId)
                    .toList();

            battlePublisher.createBattlePublish(deviceIds, CreateBattleResponseDto.builder()
                    .roomId(createBattleDto.getRoomId())
                    .battlePlayers(createBattleDto.getMatchPlayers())
                    .build());
        }
    }
}
