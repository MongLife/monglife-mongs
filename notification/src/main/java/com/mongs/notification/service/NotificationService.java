package com.mongs.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.vo.mqtt.*;
import com.mongs.notification.client.MqttClient;
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

    public void publishDelete(Long accountId, PublishDeleteVo publishDeleteVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_DELETE)
                            .data(publishDeleteVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishStroke(Long accountId, PublishStrokeVo publishStrokeVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_STROKE)
                            .data(publishStrokeVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishFeed(Long accountId, PublishFeedVo publishFeedVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_FEED)
                            .data(publishFeedVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishSleeping(Long accountId, PublishSleepingVo publishSleepingVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_SLEEPING)
                            .data(publishSleepingVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishPoop(Long accountId, PublishPoopVo publishPoopVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_POOP)
                            .data(publishPoopVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishTraining(Long accountId, PublishTrainingVo publishTrainingVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_TRAINING)
                            .data(publishTrainingVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishGraduation(Long accountId, PublishGraduationVo publishGraduationVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_GRADUATION)
                            .data(publishGraduationVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishEvolution(Long accountId, PublishEvolutionVo publishEvolutionVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_EVOLUTION)
                            .data(publishEvolutionVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishEvolutionReady(Long accountId, PublishEvolutionReadyVo publishEvolutionReadyVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_EVOLUTION_READY)
                            .data(publishEvolutionReadyVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }
    public void publishSleep(Long accountId, PublishSleepVo publishSleepVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_SLEEP)
                            .data(publishSleepVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishHealthy(Long accountId, PublishHealthyVo publishHealthyVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_HEALTH)
                            .data(publishHealthyVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishSatiety(Long accountId, PublishSatietyVo publishSatietyVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_SATIETY)
                            .data(publishSatietyVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishStrength(Long accountId, PublishStrengthVo publishStrengthVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_STRENGTH)
                            .data(publishStrengthVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }

    public void publishWeight(Long accountId, PublishWeightVo publishWeightVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_WEIGHT)
                            .data(publishWeightVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new NotificationException(NotificationErrorCode.GENERATE_DATA_FAIL);
        }
    }
}
