package com.mongs.lifecycle.service.moduleService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.enums.mqtt.PublishCode;
import com.mongs.core.vo.mqtt.*;
import com.mongs.lifecycle.client.MqttClient;
import com.mongs.lifecycle.service.moduleService.vo.*;
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

    public void publishEvolutionReady(Long accountId, PublishEvolutionReadyVo publishEvolutionReadyVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_EVOLUTION_READY)
                            .data(publishEvolutionReadyVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }

    public void publishWeight(Long accountId, PublishWeightVo publishWeightVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_WEIGHT)
                            .data(publishWeightVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }

    public void publishStrength(Long accountId, PublishStrengthVo publishStrengthVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_STRENGTH)
                            .data(publishStrengthVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }

    public void publishSatiety(Long accountId, PublishSatietyVo publishSatietyVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_SATIETY)
                            .data(publishSatietyVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }

    public void publishHealthy(Long accountId, PublishHealthyVo publishHealthyVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_HEALTHY)
                            .data(publishHealthyVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }

    public void publishSleep(Long accountId, PublishSleepVo publishSleepVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_SLEEP)
                            .data(publishSleepVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }

    public void publishPoop(Long accountId, PublishPoopVo publishPoopVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_POOP)
                            .data(publishPoopVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }

    public void publishDead(Long accountId, PublishDeadVo publishDeadVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_DEAD)
                            .data(publishDeadVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }

    public void publishState(Long accountId, PublishStateVo publishStateVo) {
        try {
            String data = objectMapper.writeValueAsString(
                    BasicPublish.builder()
                            .code(PublishCode.MONG_STATE)
                            .data(publishStateVo)
                            .build());
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, data);
        } catch (JsonProcessingException ignored) {}
    }
}
