package com.mongs.play.client.publisher.battle.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.battle.dto.res.BasicPublishDto;
import com.mongs.play.client.publisher.battle.event.MatchWaitEvent;
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

@Slf4j
@Component
@RequiredArgsConstructor
@MessagingGateway(defaultRequestChannel = "mqttInboundRoundMatchChannel")
public class MqttInboundRoundMatchClient implements MessageHandler {

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();

        log.info("{} : {}", topic, payload);
    }
}
