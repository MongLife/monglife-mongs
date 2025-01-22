package com.monglife.mongs.module.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.mongs.module.mqtt.client.MqttOutBoundClient;
import com.monglife.mongs.module.mqtt.config.MqttConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttSendService {

    private final MqttOutBoundClient mqttOutBoundClient;

    private final ObjectMapper objectMapper;

    private final MqttConfigProperties mqttConfigProperties;

    public MqttSendService(
            @Autowired MqttOutBoundClient mqttOutBoundClient,
            @Autowired MqttConfigProperties mqttConfigProperties,
            @Qualifier("moduleMqttObjectMapper") ObjectMapper objectMapper
    ) {
        this.mqttOutBoundClient = mqttOutBoundClient;
        this.objectMapper = objectMapper;
        this.mqttConfigProperties = mqttConfigProperties;
    }

    public <T> void sendMessage(String topic, T responseDto) {
        try {
            String payload = objectMapper.writeValueAsString(responseDto);
            this.sendMessage(topic, payload);
        } catch (JsonProcessingException e) {
            log.error("[MQTT] {}", e.getMessage());
        }
    }

    /**
     * 메시지 전송
     * @param topic 토픽
     * @param payload 페이로드
     */
    private void sendMessage(String topic, String payload) {

        while (topic.startsWith("/")) {
            topic = topic.substring(1);
        }

        while (topic.endsWith("/")) {
            topic = topic.substring(0, topic.length() - 1);
        }

        String sendTopic;

        if (topic.isBlank()) {
            sendTopic = mqttConfigProperties.publisher.baseTopic;
        } else {
            sendTopic = String.format("%s/%s", mqttConfigProperties.publisher.baseTopic, topic);
        }

        mqttOutBoundClient.send(sendTopic, payload);
    }
}
