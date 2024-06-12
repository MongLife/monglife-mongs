package com.mongs.play.client.publisher.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.event.client.MqttClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttService {

    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper;

    @Value("${application.mqtt.topic.mong_data}")
    private String TOPIC_FILTER;

    public void sendMong(Long mongId, Object data) {
        try {
            mqttClient.sendToMqtt(TOPIC_FILTER + "mong/" + mongId, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ignored) {}
    }

    public void sendMember(Long accountId, Object data) {
        try {
            mqttClient.sendToMqtt(TOPIC_FILTER + "member/" + accountId, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ignored) {}
    }
}
