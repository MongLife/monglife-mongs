package com.monglife.mongs.app.activity.battle.schdeuler;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.activity.battle.dto.request.CreateBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.app.activity.battle.vo.CreateBattleVo;
import com.monglife.mongs.domain.match.service.MatchingService;
import com.monglife.mongs.domain.match.vo.FindMatchingVo;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.module.mqtt.annotation.MqttPublish;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
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

    private final BattleService battleService;

    private final MatchingService matchingService;

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

            // 배틀룸 생성
            this.createBattle(CreateBattleRequestDto.builder()
                    .createBattleVoSet(createBattleVoSet)
                    .build());
        }
    }

    /**
     * 배틀룸 생성
     * @param createBattleRequestDto 매칭된 플레이어 정보
     * @return 배틀룸 생성 시, 비동기 응답
     */
    @MqttPublish("/battle/search/{topic}")
    public MqttResponseEntity<ResponseDto<CreateBattleResponseDto>> createBattle(CreateBattleRequestDto createBattleRequestDto) {

        Set<CreateBattleVo> createBattleVoSet = createBattleRequestDto.getCreateBattleVoSet();

        CreateBattleDto createBattleDto = battleService.createBattle(createBattleVoSet);

        List<String> topics = createBattleRequestDto.getCreateBattleVoSet().stream()
                .filter(createBattleVo -> !createBattleVo.getIsBot())
                .map(CreateBattleVo::getDeviceId)
                .toList();

        Long roomId = createBattleDto.getRoomId();
        Set<MatchPlayerVo> battlePlayers = createBattleDto.getBattlePlayers();

        return MqttResponseEntity
                .body(BattleResponse.APP_ACTIVITY_BATTLE_FIND_MATCHING.toResponseDto(CreateBattleResponseDto.builder()
                        .roomId(roomId)
                        .battlePlayers(battlePlayers)
                        .build()))
                .topics(topics);
    }
}
