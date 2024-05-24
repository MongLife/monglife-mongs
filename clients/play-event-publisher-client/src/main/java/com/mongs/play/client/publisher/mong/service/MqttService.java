package com.mongs.play.client.publisher.mong.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.mong.client.MqttClient;
import com.mongs.play.client.publisher.mong.code.PublishCode;
import com.mongs.play.client.publisher.mong.dto.res.BasicPublishDto;
import com.mongs.play.client.publisher.mong.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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
