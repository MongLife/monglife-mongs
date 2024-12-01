package com.monglife.mongs.module.mqtt.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MqttPublishAspect {

    private final MqttSendService mqttSendService;

    private final ObjectMapper objectMapper;

    @Autowired
    public MqttPublishAspect(MqttSendService mqttSendService, ObjectMapper objectMapper) {
        this.mqttSendService = mqttSendService;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Pointcut("@annotation(com.monglife.mongs.module.mqtt.annotation.MqttPublish)")
    private void mqttPublishPointcut() {}

    @Pointcut("execution(com.monglife.mongs.module.mqtt.dto.MqttResponseEntity *(..))")
    private void executionPointcut() {}

    @AfterReturning(value = "mqttPublishPointcut() && executionPointcut()", returning = "mqttResponseEntity")
    public void after(JoinPoint joinPoint, MqttResponseEntity<?> mqttResponseEntity) {

        log.info("{}", mqttResponseEntity);

        mqttResponseEntity.getTopics().forEach(topic -> {
            mqttSendService.sendMessage(topic, mqttResponseEntity.getBody());
        });
    }
}
