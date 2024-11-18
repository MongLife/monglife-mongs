package com.monglife.mongs.app.battle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.mongs.app.battle.dto.etc.*;
import com.monglife.mongs.app.battle.dto.request.BattleRequestDto;
import com.monglife.mongs.app.battle.global.exception.NotExistsWaitMatchingException;
import com.monglife.mongs.app.battle.service.BattleService;
import com.monglife.mongs.app.battle.service.MatchingService;
import com.monglife.mongs.app.battle.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@MessagingGateway(defaultRequestChannel = "mqttInboundChannel")
public class BattleController implements MessageHandler {

    private final MatchingService matchingService;

    private final BattleService battleService;

    private final MqttSendService mqttSendService;

    private final ObjectMapper objectMapper;


    @Scheduled(fixedDelay = 1000)
    public void findMatching() {
        while (true) {
            try {
                Set<FindMatchingDto> findMatchingDtoSet = matchingService.findWaitMatching();

                Set<CreateBattleDto> createBattleDtoSet = findMatchingDtoSet.stream()
                                .map(findMatchingDto -> CreateBattleDto.builder()
                                        .mongId(findMatchingDto.getMongId())
                                        .deviceId(findMatchingDto.getDeviceId())
                                        .accountId(findMatchingDto.getAccountId())
                                        .isBot(findMatchingDto.getIsBot())
                                        .build())
                                .collect(Collectors.toSet());

                // 배틀 룸 생성
                battleService.createBattle(createBattleDtoSet);

                // 배틀 생성 전송
                mqttSendService.createBattleSend(createBattleDtoSet);

            } catch (NotExistsWaitMatchingException e) {
                break;
            }
        }
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();

        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(BattleRequestDto.class, Object.class);

            BattleRequestDto<Object> battleRequestDto = objectMapper.readValue(payload, type);

            switch (battleRequestDto.getCode()) {

                case BATTLE_ENTER -> {
                    EnterBattleDto enterBattleDto
                            = objectMapper.convertValue(battleRequestDto.getData(), new TypeReference<EnterBattleDto>() {});

                    battleService.enterBattle(enterBattleDto);
                }

                case BATTLE_EXIT -> {
                    ExitBattleDto exitBattleDto
                            = objectMapper.convertValue(battleRequestDto.getData(), new TypeReference<ExitBattleDto>() {});

                    battleService.exitBattle(exitBattleDto);
                }

                case BATTLE_PICK -> {
                    PickBattleDto pickBattleDto
                            = objectMapper.convertValue(battleRequestDto.getData(), new TypeReference<PickBattleDto>() {});

                    battleService.pickBattle(pickBattleDto);
                }

                default -> {}
            }

        } catch (JsonProcessingException ignore) {}
    }
}
