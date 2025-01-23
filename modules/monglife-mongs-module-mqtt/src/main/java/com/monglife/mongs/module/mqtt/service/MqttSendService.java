package com.monglife.mongs.module.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.mongs.module.mqtt.client.MqttOutBoundClient;
import com.monglife.mongs.module.mqtt.config.MqttConfigProperties;
import com.monglife.mongs.module.mqtt.utils.TopicUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttSendService {

    private final MqttConfigProperties mqttConfigProperties;

    private final MqttOutBoundClient mqttOutBoundClient;

    private final ObjectMapper objectMapper;

    public MqttSendService(
            @Autowired MqttConfigProperties mqttConfigProperties,
            @Autowired MqttOutBoundClient mqttOutBoundClient,
            @Qualifier("moduleMqttObjectMapper") ObjectMapper objectMapper
    ) {
        this.mqttConfigProperties = mqttConfigProperties;
        this.mqttOutBoundClient = mqttOutBoundClient;
        this.objectMapper = objectMapper;
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

        String baseTopic = TopicUtil.preProcessTopic(mqttConfigProperties.publisher.baseTopic);

        String sendTopic = TopicUtil.generateTopic(baseTopic, topic);

        mqttOutBoundClient.send(TopicUtil.preProcessTopic(sendTopic), payload);
    }
}
