package com.monglife.mongs.client.mqtt.data.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.mongs.client.mqtt.business.event.MatchEnterEvent;
import com.monglife.mongs.client.mqtt.business.event.MatchExitEvent;
import com.monglife.mongs.client.mqtt.business.event.MatchPickEvent;
import com.monglife.mongs.client.mqtt.business.vo.req.MatchEnterVo;
import com.monglife.mongs.client.mqtt.business.vo.req.MatchExitVo;
import com.monglife.mongs.client.mqtt.business.vo.req.MatchPickVo;
import com.monglife.mongs.client.mqtt.data.dto.BasicPublishBattleDto;
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
@MessagingGateway(defaultRequestChannel = "mqttBattleInboundMatchChannel")
public class MqttBattleInboundMatchClient implements MessageHandler {

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();

        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(BasicPublishBattleDto.class, Object.class);
            BasicPublishBattleDto<Object> basicPublishBattleDto = objectMapper.readValue(payload, javaType);
            log.info("{} : {} : {}", topic, payload, basicPublishBattleDto);

            switch (basicPublishBattleDto.getCode()) {
                case MATCH_ENTER -> {
                    MatchEnterVo matchEnterVo = objectMapper.convertValue(basicPublishBattleDto.getData(), new TypeReference<MatchEnterVo>() { });
                    publisher.publishEvent(MatchEnterEvent.builder()
                            .roomId(matchEnterVo.roomId())
                            .playerId(matchEnterVo.playerId())
                            .build());
                }

                case MATCH_PICK -> {
                    MatchPickVo matchPickVo = objectMapper.convertValue(basicPublishBattleDto.getData(), new TypeReference<MatchPickVo>() { });
                    publisher.publishEvent(MatchPickEvent.builder()
                            .roomId(matchPickVo.roomId())
                            .playerId(matchPickVo.playerId())
                            .pick(matchPickVo.pick())
                            .targetPlayerId(matchPickVo.targetPlayerId())
                            .build());
                }

                case MATCH_EXIT -> {
                    MatchExitVo matchExitVo = objectMapper.convertValue(basicPublishBattleDto.getData(), new TypeReference<MatchExitVo>() { });
                    publisher.publishEvent(MatchExitEvent.builder()
                            .roomId(matchExitVo.roomId())
                            .playerId(matchExitVo.playerId())
                            .build());
                }

                default -> {}
            }
        } catch (JsonProcessingException ignored) {
        }
    }
}
