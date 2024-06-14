package com.mongs.play.client.publisher.battle.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.battle.dto.BasicPublishDto;
import com.mongs.play.client.publisher.battle.event.MatchEnterEvent;
import com.mongs.play.client.publisher.battle.event.MatchExitEvent;
import com.mongs.play.client.publisher.battle.event.MatchPickEvent;
import com.mongs.play.client.publisher.battle.vo.req.MatchEnterVo;
import com.mongs.play.client.publisher.battle.vo.req.MatchExitVo;
import com.mongs.play.client.publisher.battle.vo.req.MatchPickVo;
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
@MessagingGateway(defaultRequestChannel = "mqttInboundMatchChannel")
public class MqttInboundMatchClient implements MessageHandler {

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(BasicPublishDto.class, MatchEnterVo.class);
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();

        try {
            BasicPublishDto<Object> basicPublishDto = objectMapper.readValue(payload, javaType);
            log.info("{} : {} : {}", topic, payload, basicPublishDto);

            switch (basicPublishDto.getCode()) {
                case MATCH_ENTER -> {
                    MatchEnterVo matchEnterVo = (MatchEnterVo) basicPublishDto.getData();
                    publisher.publishEvent(MatchEnterEvent.builder()
                            .roomId(matchEnterVo.roomId())
                            .playerId(matchEnterVo.playerId())
                            .build());
                }

                case MATCH_PICK -> {
                    MatchPickVo matchPickVo = (MatchPickVo) basicPublishDto.getData();
                    publisher.publishEvent(MatchPickEvent.builder()
                            .roomId(matchPickVo.roomId())
                            .playerId(matchPickVo.playerId())
                            .pick(matchPickVo.pick())
                            .build());
                }

                case MATCH_EXIT -> {
                    MatchExitVo matchExitVo = (MatchExitVo) basicPublishDto.getData();
                    publisher.publishEvent(MatchExitEvent.builder()
                            .roomId(matchExitVo.roomId())
                            .playerId(matchExitVo.playerId())
                            .build());
                }

                default -> {}
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
