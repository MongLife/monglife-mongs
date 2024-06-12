package com.mongs.play.client.publisher.battle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.battle.client.MqttOutboundClient;
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

    public void sendSearchMatchFail(Long mongId, Object data) {
        try {
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/search/" + mongId, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ignored) {}
    }

    public void sendFindMatch(Long mongIdA, Long mongIdB, Object data) {
        try {
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/search/" + mongIdA, objectMapper.writeValueAsString(data));
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/search/" + mongIdB, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ignored) {}
    }

    public void sendStartMatch(String roomId, Object data) {
        try {
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/round/" + roomId, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ignored) {}
    }

    public void sendRoundOver(String roomId, Object data) {
        try {
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/round/" + roomId, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ignored) {}
    }

    public void sendMatchOver(String roomId, Object data) {
        try {
            mqttOutboundClient.sendToMqtt(TOPIC_FILTER + "battle/match/round/" + roomId, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ignored) {}
    }
}
