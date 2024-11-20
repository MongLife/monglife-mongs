package com.monglife.mongs.app.battle.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.battle.controller.BattleController;
import com.monglife.mongs.app.battle.dto.request.EnterBattleRequestDto;
import com.monglife.mongs.app.battle.dto.request.ExitBattleRequestDto;
import com.monglife.mongs.app.battle.dto.request.PickBattleRequestDto;
import com.monglife.mongs.app.battle.dto.response.FightBattleResponseDto;
import com.monglife.mongs.app.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.battle.global.dto.BattleRequestDto;
import com.monglife.mongs.app.battle.global.dto.BattleResponseDto;
import com.monglife.mongs.app.battle.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@MessagingGateway(defaultRequestChannel = "mqttInboundChannel")
public class MqttInBoundClient implements MessageHandler {

    private final BattleController battleController;

    private final MqttSendService mqttSendService;

    private final ObjectMapper objectMapper;


    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();

        log.info("[MqttInBoundClient] [handleMessage] {} : {}", topic, payload);

        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(BattleRequestDto.class, Object.class);

            BattleRequestDto<Object> battleRequestDto = objectMapper.readValue(payload, type);

            switch (battleRequestDto.getCode()) {
                case BATTLE_ENTER -> {
                    EnterBattleRequestDto enterBattleRequestDto
                            = objectMapper.convertValue(battleRequestDto.getData(), new TypeReference<EnterBattleRequestDto>() {});

                    ResponseDto<BattleResponseDto<FightBattleResponseDto>> responseDto = battleController.enterBattle(enterBattleRequestDto);

                    if (HttpStatus.OK.value() == responseDto.getHttpStatus()) {
                        // 게임 시작 MQTT 응답
                        mqttSendService.sendMessage(responseDto);
                    }

                }
                case BATTLE_EXIT -> {
                    ExitBattleRequestDto exitBattleRequestDto
                            = objectMapper.convertValue(battleRequestDto.getData(), new TypeReference<ExitBattleRequestDto>() {});

                    ResponseDto<BattleResponseDto<OverBattleResponseDto>> responseDto = battleController.exitBattle(exitBattleRequestDto);

                    if (HttpStatus.OK.value() == responseDto.getHttpStatus()) {
                        // 게임 종료 MQTT 응답
                        mqttSendService.sendMessage(responseDto);
                    }
                }
                case BATTLE_PICK -> {
                    PickBattleRequestDto pickBattleRequestDto
                            = objectMapper.convertValue(battleRequestDto.getData(), new TypeReference<PickBattleRequestDto>() {});

                    ResponseDto<BattleResponseDto<FightBattleResponseDto>> responseDto = battleController.pickBattle(pickBattleRequestDto);

                    if (HttpStatus.OK.value() == responseDto.getHttpStatus()) {
                        // 라운드 종료 MQTT 응답
                        mqttSendService.sendMessage(responseDto);
                    }
                }
                default -> {}
            }

        } catch (JsonProcessingException e) {
            log.error("[MqttInBoundClient] [handleMessage] {} : {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
