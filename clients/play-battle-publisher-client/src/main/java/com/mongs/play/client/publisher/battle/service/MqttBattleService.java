package com.mongs.play.client.publisher.battle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.battle.client.MqttBattleOutboundClient;
import com.mongs.play.client.publisher.battle.code.PublishBattleCode;
import com.mongs.play.client.publisher.battle.dto.BasicPublishBattleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttBattleService {

    private final MqttBattleOutboundClient mqttBattleOutboundClient;
    private final ObjectMapper objectMapper;

    @Value("${application.mqtt.topic.mong_data}")
    private String TOPIC_FILTER;

    public void sendMatchFind(String deviceId, Object data) {
        try {
            String dataJson = objectMapper.writeValueAsString(BasicPublishBattleDto.builder().code(PublishBattleCode.MATCH_FIND).data(data).build());
            mqttBattleOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/search/" + deviceId, dataJson);
        } catch (JsonProcessingException ignored) {}
    }

    public void sendMatch(String roomId, Object data) {
        try {
            String dataJson = objectMapper.writeValueAsString(BasicPublishBattleDto.builder().code(PublishBattleCode.MATCH).data(data).build());
            mqttBattleOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/" + roomId, dataJson);
        } catch (JsonProcessingException ignored) {}
    }

    public void sendMatchOver(String roomId, Object data) {
        try {
            String dataJson = objectMapper.writeValueAsString(BasicPublishBattleDto.builder().code(PublishBattleCode.MATCH_OVER).data(data).build());
            mqttBattleOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/" + roomId, dataJson);
        } catch (JsonProcessingException ignored) {}
    }
}
