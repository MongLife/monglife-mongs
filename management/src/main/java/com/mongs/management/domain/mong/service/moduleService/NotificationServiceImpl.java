package com.mongs.management.domain.mong.service.moduleService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.enums.mqtt.PublishCode;
import com.mongs.core.vo.mqtt.*;
import com.mongs.management.domain.mong.client.MqttClient;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper;

    @Value("${application.mqtt.topic.mong_data}")
    private String TOPIC_FILTER;

    @Override
    public void publishEvolutionReady(Long accountId, PublishEvolutionReadyVo publishEvolutionReadyVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_EVOLUTION_READY)
                            .data(publishEvolutionReadyVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }
    
    @Override
    public void publishCreate(Long accountId, PublishCreateVo publishCreateVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_CREATE)
                            .data(publishCreateVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }

    @Override
    public void publishDelete(Long accountId, PublishDeleteVo publishDeleteVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_DELETE)
                            .data(publishDeleteVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }

    @Override
    public void publishStroke(Long accountId, PublishStrokeVo publishStrokeVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_STROKE)
                            .data(publishStrokeVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }

    @Override
    public void publishFeed(Long accountId, PublishFeedVo publishFeedVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_FEED)
                            .data(publishFeedVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }

    @Override
    public void publishSleeping(Long accountId, PublishSleepingVo publishSleepingVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_SLEEPING)
                            .data(publishSleepingVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }

    @Override
    public void publishPoop(Long accountId, PublishPoopVo publishPoopVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_POOP)
                            .data(publishPoopVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }

    @Override
    public void publishTraining(Long accountId, PublishTrainingVo publishTrainingVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_TRAINING)
                            .data(publishTrainingVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }

    @Override
    public void publishGraduation(Long accountId, PublishGraduationVo publishGraduationVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_GRADUATION)
                            .data(publishGraduationVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }

    @Override
    public void publishEvolution(Long accountId, PublishEvolutionVo publishEvolutionVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_EVOLUTION)
                            .data(publishEvolutionVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException e) {
            throw new ManagementException(ManagementErrorCode.GENERATE_DATA_FAIL);
        }
    }
}
