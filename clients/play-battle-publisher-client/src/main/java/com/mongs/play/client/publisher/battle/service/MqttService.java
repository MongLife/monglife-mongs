package com.mongs.play.client.publisher.battle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.battle.client.MqttOutboundClient;
import com.mongs.play.client.publisher.battle.code.PublishCode;
import com.mongs.play.client.publisher.battle.dto.BasicPublishDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MqttService {

    private final MqttOutboundClient mqttOutboundClient;
    private final ObjectMapper objectMapper;

    @Value("${application.mqtt.topic.mong_data}")
    private String TOPIC_FILTER;

    public void sendMatchFind(String deviceId, Object data) {
        try {
            String dataJson = objectMapper.writeValueAsString(BasicPublishDto.builder().code(PublishCode.MATCH_FIND).data(data).build());
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/search/" + deviceId, dataJson);
        } catch (JsonProcessingException ignored) {}
    }

    public void sendRound(String roomId, Object data) {
        try {
            String dataJson = objectMapper.writeValueAsString(BasicPublishDto.builder().code(PublishCode.MATCH).data(data).build());
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/" + roomId, dataJson);
        } catch (JsonProcessingException ignored) {}
    }

    public void sendMatchOver(String roomId, Object data) {
        try {
            String dataJson = objectMapper.writeValueAsString(BasicPublishDto.builder().code(PublishCode.MATCH_OVER).data(data).build());
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/" + roomId, dataJson);
        } catch (JsonProcessingException ignored) {}
    }
}
