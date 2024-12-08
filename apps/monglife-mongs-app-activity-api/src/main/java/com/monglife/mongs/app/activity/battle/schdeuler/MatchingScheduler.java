package com.monglife.mongs.app.activity.battle.schdeuler;

import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import com.monglife.mongs.domain.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.domain.battle.exception.NotExistsMongIdException;
import com.monglife.mongs.domain.battle.exception.NotExistsMongTypeCodeException;
import com.monglife.mongs.domain.battle.exception.NotExistsWaitMatchingException;
import com.monglife.mongs.domain.battle.repository.ComnCodeRepository;
import com.monglife.mongs.domain.battle.service.BattleService;
import com.monglife.mongs.domain.battle.service.MatchingService;
import com.monglife.mongs.domain.battle.vo.BattlePlayerVo;
import com.monglife.mongs.domain.battle.vo.CreateBattleVo;
import com.monglife.mongs.domain.battle.vo.FindMatchingVo;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MatchingScheduler {

    @Value("${application.scheduler.matching.bot-mong-type-group-code}")
    private String BOT_MONG_TYPE_GROUP_CODE;

    private final MqttSendService mqttSendService;

    private final MatchingService matchingService;

    private final BattleService battleService;

    private final MongService mongService;

    private final ComnCodeRepository comnCodeRepository;


    @Scheduled(fixedDelay = 1000)
    public void findMatching() {
        while (true) {
            try {
                Set<FindMatchingVo> findMatchingVoSet = matchingService.findWaitMatching();

                Set<CreateBattleVo> createBattleVoSet = new HashSet<>();

                for (FindMatchingVo findMatchingVo : findMatchingVoSet) {
                    if (!findMatchingVo.getIsBot()) {
                            GetMongDto getMongDto = mongService.getMong(findMatchingVo.getMongId());
                        try {
                                createBattleVoSet.add(CreateBattleVo.builder()
                                        .playerId(UUID.randomUUID().toString().replace("-", ""))
                                        .deviceId(findMatchingVo.getDeviceId())
                                        .accountId(findMatchingVo.getAccountId())
                                        .mongId(findMatchingVo.getMongId())
                                        .mongTypeCode(getMongDto.getMongTypeCode())
                                        .weight(getMongDto.getWeight())
                                        .strength(getMongDto.getStrength())
                                        .fatigue(getMongDto.getFatigue())
                                        .isBot(findMatchingVo.getIsBot())
                                        .build());
                        } catch (NotExistsMongIdException e) {
                            for (FindMatchingVo addFindMatchingVo : findMatchingVoSet) {
                                if (!findMatchingVo.getMongId().equals(addFindMatchingVo.getMongId()) && !addFindMatchingVo.getIsBot()) {
                                    matchingService.createWaitMatching(addFindMatchingVo.getAccountId(), addFindMatchingVo.getDeviceId(), addFindMatchingVo.getMongId());
                                }
                            }
                            return;
                        }
                    } else {
                        String mongTypeCode = comnCodeRepository.findByGroupCode(BOT_MONG_TYPE_GROUP_CODE).stream()
                                .findAny()
                                .orElseThrow(NotExistsMongTypeCodeException::new)
                                .getComnCode();

                        createBattleVoSet.add(CreateBattleVo.builder()
                                .playerId(UUID.randomUUID().toString().replace("-", ""))
                                .deviceId(findMatchingVo.getDeviceId())
                                .accountId(findMatchingVo.getAccountId())
                                .mongId(findMatchingVo.getMongId())
                                .mongTypeCode(mongTypeCode)
                                .weight(0D)
                                .strength(0D)
                                .fatigue(0D)
                                .isBot(findMatchingVo.getIsBot())
                                .build());
                    }
                }

                List<String> topics = createBattleVoSet.stream()
                        .filter(createBattleVo -> !createBattleVo.getIsBot())
                        .map(createBattleVo -> "search/" + createBattleVo.getDeviceId())
                        .toList();

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

            } catch (NotExistsWaitMatchingException e) {
                break;
            }
        }
    }
}
