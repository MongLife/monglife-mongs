package com.mongs.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.vo.mqtt.PublishCreateVo;
import com.mongs.core.vo.mqtt.PublishShiftVo;
import com.mongs.core.vo.mqtt.PublishStateVo;
import com.mongs.notification.client.MqttClient;
import com.mongs.core.vo.mqtt.PublishStatusVo;
import com.mongs.notification.code.PublishCode;
import com.mongs.notification.controller.dto.response.BasicPublish;
import com.mongs.notification.exception.NotificationErrorCode;
import com.mongs.notification.exception.NotificationException;
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

    public void publishCreate(Long accountId, PublishCreateVo publishCreateVo) {
        try {
            log.info("{}", publishCreateVo);
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_CREATE)
                            .data(publishCreateVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishStatus(Long accountId, PublishStatusVo publishStatusVo) {
        try {
            log.info("{}", publishStatusVo);
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_STATUS)
                            .data(publishStatusVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishShift(Long accountId, PublishShiftVo publishShiftVo) {
        try {
            log.info("{}", publishShiftVo);
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_SHIFT)
                            .data(publishShiftVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishState(Long accountId, PublishStateVo publishStateVo) {
        try {
            log.info("{}", publishStateVo);
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_STATE)
                            .data(publishStateVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }
}
