package com.mongs.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.mqtt.PublishCreate;
import com.mongs.core.mqtt.PublishShift;
import com.mongs.core.mqtt.PublishState;
import com.mongs.notification.client.MqttClient;
import com.mongs.core.mqtt.PublishStatus;
import com.mongs.notification.code.PublishCode;
import com.mongs.notification.dto.request.BasicPublish;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper;

    @Value("${application.mqtt.topic.mong_data}")
    private String TOPIC_FILTER;

    public void publishCreate(String email, PublishCreate publishCreate) throws JsonProcessingException {
        String data = objectMapper.writeValueAsString(
                BasicPublish.builder()
                        .code(PublishCode.MONG_CREATE)
                        .data(publishCreate)
                        .build()
        );

        mqttClient.sendToMqtt(TOPIC_FILTER + email, data);
    }

    public void publishStatus(String email, PublishStatus publishStatus) throws JsonProcessingException {
        log.info("{}", publishStatus);
        String data = objectMapper.writeValueAsString(
                BasicPublish.builder()
                        .code(PublishCode.MONG_STATUS)
                        .data(publishStatus)
                        .build()
        );

        mqttClient.sendToMqtt(TOPIC_FILTER + email, data);
    }

    public void publishShift(String email, PublishShift publishShift) throws JsonProcessingException {
        log.info("{}", publishShift);
        String data = objectMapper.writeValueAsString(
                BasicPublish.builder()
                        .code(PublishCode.MONG_SHIFT)
                        .data(publishShift)
                        .build()
        );

        mqttClient.sendToMqtt(TOPIC_FILTER + email, data);
    }

    public void publishState(String email, PublishState publishState) throws JsonProcessingException {
        log.info("{}", publishState);
        String data = objectMapper.writeValueAsString(
                BasicPublish.builder()
                        .code(PublishCode.MONG_STATE)
                        .data(publishState)
                        .build()
        );

        mqttClient.sendToMqtt(TOPIC_FILTER + email, data);
    }
}
