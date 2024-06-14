package com.mongs.play.client.publisher.battle.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.battle.dto.BasicPublishDto;
import com.mongs.play.client.publisher.battle.event.MatchWaitEvent;
import com.mongs.play.client.publisher.battle.vo.req.MatchWaitVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Slf4j
@RequiredArgsConstructor
@MessagingGateway(defaultRequestChannel = "mqttInboundSearchChannel")
public class MqttInboundSearchClient implements MessageHandler {

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(BasicPublishDto.class, MatchWaitVo.class);
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();

        try {
            BasicPublishDto<Object> basicPublishDto = objectMapper.readValue(payload, javaType);

            log.info("{} : {} : {}", topic, payload, basicPublishDto);

            switch (basicPublishDto.getCode()) {
                case MATCH_WAIT -> {
                    MatchWaitVo matchWaitVo = (MatchWaitVo) basicPublishDto.getData();
                    publisher.publishEvent(MatchWaitEvent.builder()
                            .deviceId(matchWaitVo.deviceId())
                            .mongId(matchWaitVo.mongId())
                            .build());
                }

                default -> {}
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
