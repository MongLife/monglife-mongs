package com.monglife.mongs.app.activity.battle.schdeuler;

import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import com.monglife.mongs.domain.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.domain.battle.exception.NotExistsMongIdException;
import com.monglife.mongs.domain.battle.exception.NotExistsWaitMatchingException;
import com.monglife.mongs.domain.battle.service.BattleService;
import com.monglife.mongs.domain.battle.service.MatchingService;
import com.monglife.mongs.domain.battle.vo.BattlePlayerVo;
import com.monglife.mongs.domain.battle.vo.CreateBattleVo;
import com.monglife.mongs.domain.battle.vo.FindMatchingVo;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

            Set<CreateBattleVo> createBattleVoSet;

            try {
                Set<FindMatchingVo> findMatchingVoSet = matchingService.findWaitMatching();

                createBattleVoSet = findMatchingVoSet.stream()
                        .map(findMatchingVo -> CreateBattleVo.builder()
                                .playerId(UUID.randomUUID().toString().replace("-", ""))
                                .deviceId(findMatchingVo.getDeviceId())
                                .accountId(findMatchingVo.getAccountId())
                                .mongId(findMatchingVo.getMongId())
                                // TODO: MongStatus 조회해서 가져와야함
                                .mongTypeCode("CH1000")
                                .weightRatio(0D)
                                .strengthRatio(0D)
                                .fatigueRatio(0D)
                                .isBot(findMatchingVo.getIsBot())
                                .build())
                        .collect(Collectors.toSet());

            } catch (NotExistsWaitMatchingException e) {
                break;
            }

            List<String> topics = createBattleVoSet.stream()
                    .filter(createBattleVo -> !createBattleVo.getIsBot())
                    .map(createBattleVo -> "battle/search/" + createBattleVo.getDeviceId())
                    .toList();

            try {
                // 배틀 룸 생성
                CreateBattleDto createBattleDto = battleService.createBattle(createBattleVoSet);

                Long roomId = createBattleDto.getRoomId();
                Set<BattlePlayerVo> battlePlayers = createBattleDto.getBattlePlayers();

                CreateBattleResponseDto createBattleResponseDto = CreateBattleResponseDto.builder()
                        .roomId(roomId)
                        .battlePlayers(battlePlayers)
                        .build();

                // 배틀 생성 전송
                mqttSendService.sendMessage(MqttResponseEntity
                        .body(BattleResponse.ACTIVITY_BATTLE_FIND_MATCHING.toResponseDto(createBattleResponseDto))
                        .topics(topics));

                log.info("[MatchingScheduler] [findMatching] {}", createBattleResponseDto);

            } catch (NotExistsMongIdException e) {

                mqttSendService.sendMessage(MqttResponseEntity
                        .body(BattleResponse.ACTIVITY_BATTLE_NOT_EXISTS_MONG_ID.toResponseDto(e.getResult()))
                        .topics(topics));

                // 배틀 생성 실패 전송
                log.error("[MatchingScheduler] [findMatching] {}", e.getResult());

            } catch (RuntimeException e) {
                log.error("[MatchingScheduler] [findMatching] {} : {}", e.getClass().getSimpleName(), e.getMessage());
                break;
            }
        }
    }
}
