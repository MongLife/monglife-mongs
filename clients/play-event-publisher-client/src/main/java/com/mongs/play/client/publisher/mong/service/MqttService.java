package com.mongs.play.client.publisher.mong.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.mong.client.MqttClient;
import com.mongs.play.client.publisher.mong.code.PublishCode;
import com.mongs.play.client.publisher.mong.dto.res.BasicPublishDto;
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

    public void send(PublishCode publishCode, Long accountId, Object data) {
        try {
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, objectMapper.writeValueAsString(
                    BasicPublishDto.builder()
                            .code(publishCode)
                            .data(data)
                            .build()));
        } catch (JsonProcessingException ignored) {}
    }
}
