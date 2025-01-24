package com.monglife.mongs.app.activity.battle.schdeuler;

import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.publisher.BattlePublisher;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.app.activity.battle.vo.CreateBattleVo;
import com.monglife.mongs.domain.match.service.MatchingService;
import com.monglife.mongs.domain.match.vo.FindMatchingVo;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleMatchingScheduler {

    private final MatchingService matchingService;

    private final BattlePublisher battlePublisher;

    private final BattleService battleService;

    /**
     * 배틀 매칭 스케줄러
     */
    @Scheduled(fixedDelay = 1000)
    public void matchingSchedule() {

        while (true) {
            Set<FindMatchingVo> findMatchingVoSet = matchingService.findWaitMatching();

            if (findMatchingVoSet.isEmpty()) break;

            // 매칭 예정 플레이어 Set
            Set<CreateBattleVo> createBattleVoSet = findMatchingVoSet.stream()
                    .map(findMatchingVo -> CreateBattleVo.builder()
                            .deviceId(findMatchingVo.getDeviceId())
                            .accountId(findMatchingVo.getAccountId())
                            .mongId(findMatchingVo.getMongId())
                            .isBot(findMatchingVo.getIsBot())
                            .build())
                    .collect(Collectors.toSet());

            CreateBattleDto createBattleDto = battleService.createBattle(createBattleVoSet);

            List<String> deviceIds = createBattleVoSet.stream()
                    .filter(createBattleVo -> !createBattleVo.getIsBot())
                    .map(CreateBattleVo::getDeviceId)
                    .toList();

            Long roomId = createBattleDto.getRoomId();
            Set<MatchPlayerVo> battlePlayers = createBattleDto.getBattlePlayers();

            // 배틀룸 생성
            battlePublisher.createBattlePublish(deviceIds, CreateBattleResponseDto.builder()
                    .roomId(roomId)
                    .battlePlayers(battlePlayers)
                    .build());
        }
    }
}
