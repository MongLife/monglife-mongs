package com.monglife.mongs.app.activity.battle.schdeuler;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.FindMatchingDto;
import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.exception.NotExistsWaitMatchingException;
import com.monglife.mongs.app.activity.battle.vo.BattlePlayerVo;
import com.monglife.mongs.app.activity.battle.dto.response.BattleResponseDto;
import com.monglife.mongs.app.activity.global.enums.ActivityResponse;
import com.monglife.mongs.app.activity.battle.enums.BattleStateCode;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.app.activity.battle.service.MatchingService;
import com.monglife.mongs.app.activity.battle.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchingScheduler {

    private final MatchingService matchingService;

    private final BattleService battleService;

    private final MqttSendService mqttSendService;


    @Scheduled(fixedDelay = 1000)
    public void findMatching() {
        while (true) {
            try {
                Set<FindMatchingDto> findMatchingDtoSet = matchingService.findWaitMatching();

                Set<CreateBattleDto> createBattleDtoSet = findMatchingDtoSet.stream()
                        .map(findMatchingDto -> CreateBattleDto.builder()
                                .playerId(UUID.randomUUID().toString().replace("-", ""))
                                .deviceId(findMatchingDto.getDeviceId())
                                .accountId(findMatchingDto.getAccountId())
                                .mongId(findMatchingDto.getMongId())
                                .isBot(findMatchingDto.getIsBot())
                                .build())
                        .collect(Collectors.toSet());

                // 배틀 룸 생성
                Pair<Long, Set<BattlePlayerVo>> createBattlePair = battleService.createBattle(createBattleDtoSet);

                Long roomId = createBattlePair.a;
                Set<BattlePlayerVo> battlePlayers = createBattlePair.b;

                CreateBattleResponseDto createBattleResponseDto = CreateBattleResponseDto.builder()
                        .roomId(roomId)
                        .battlePlayers(battlePlayers)
                        .build();

                List<String> topics = createBattleDtoSet.stream()
                        .filter(createBattleDto -> !createBattleDto.getIsBot())
                        .map(CreateBattleDto::getDeviceId)
                        .toList();

                BattleResponseDto<CreateBattleResponseDto> battleResponseDto = BattleResponseDto.<CreateBattleResponseDto>builder()
                        .code(BattleStateCode.BATTLE_CREATE)
                        .topics(topics)
                        .data(createBattleResponseDto)
                        .build();

                ResponseDto<BattleResponseDto<CreateBattleResponseDto>> responseDto
                        = ActivityResponse.ACTIVITY_BATTLE_FIND_MATCHING.toResponseDto(battleResponseDto);

                // 배틀 생성 전송
                mqttSendService.sendMessage(responseDto);

            } catch (NotExistsWaitMatchingException e) {
                break;
            } catch (RuntimeException e) {
                log.error("[MatchingScheduler] [findMatching] {} : {}", e.getClass().getSimpleName(), e.getMessage());
                break;
            }
        }
    }
}
