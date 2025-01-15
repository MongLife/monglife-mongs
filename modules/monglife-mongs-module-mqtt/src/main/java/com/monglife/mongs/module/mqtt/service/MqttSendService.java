package com.monglife.mongs.module.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.mongs.module.mqtt.client.MqttOutBoundClient;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttSendService {

    @Value("${spring.mqtt.base-topic}")
    private String BASE_TOPIC;

    private final MqttOutBoundClient mqttOutBoundClient;

    private final ObjectMapper objectMapper;


    public void sendMessage(String topic, String body) {
        if (topic.startsWith("/")) topic = topic.substring(1);
        if (topic.endsWith("/")) topic = topic.substring(0, topic.length() - 1);

        String baseTopic = BASE_TOPIC;
        String sendTopic = String.format("%s/%s", baseTopic, topic);

        mqttOutBoundClient.send(sendTopic, body);
    }

    public <T> void sendMessage(String topic, T responseDto) {
        try {
            String body = objectMapper.writeValueAsString(responseDto);
            this.sendMessage(topic, body);
        } catch (JsonProcessingException e) {
            log.error("[MQTT] {}", e.getMessage());
        }
    }

    public <T> void sendMessage(MqttResponseEntity<T> mqttResponseEntity) {
        mqttResponseEntity.getTopics().forEach(topic -> {
            try {
                String body = objectMapper.writeValueAsString(mqttResponseEntity.getBody());
                this.sendMessage(topic, body);
            } catch (JsonProcessingException e) {
                log.error("[MQTT] {}", e.getMessage());
            }
        });
    }
}
