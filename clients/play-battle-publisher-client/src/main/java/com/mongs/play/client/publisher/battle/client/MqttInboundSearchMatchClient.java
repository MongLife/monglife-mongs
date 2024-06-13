package com.mongs.play.client.publisher.battle.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.battle.dto.res.BasicPublishDto;
import com.mongs.play.client.publisher.battle.event.MatchWaitEvent;
import com.mongs.play.client.publisher.battle.service.EventService;
import com.mongs.play.client.publisher.battle.vo.MatchWaitVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Slf4j
@RequiredArgsConstructor
@MessagingGateway(defaultRequestChannel = "mqttInboundSearchMatchChannel")
public class MqttInboundSearchMatchClient implements MessageHandler {

    private final EventService eventService;
    private final ObjectMapper objectMapper;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(BasicPublishDto.class, MatchWaitVo.class);
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();

        try {
            BasicPublishDto<Object> basicPublishDto = objectMapper.readValue(payload, javaType);

            switch (basicPublishDto.getCode()) {
                case MATCH_WAIT -> {
                    log.info("{} : {} : {}", topic, payload, basicPublishDto);
                    MatchWaitVo matchWaitVo = (MatchWaitVo) basicPublishDto.getData();
                    eventService.matchWaitEventPublish(matchWaitVo);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
