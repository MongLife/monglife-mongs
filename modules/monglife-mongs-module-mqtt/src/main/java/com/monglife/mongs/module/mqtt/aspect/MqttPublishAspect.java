package com.monglife.mongs.module.mqtt.aspect;

import com.monglife.mongs.module.mqtt.annotation.MqttPublish;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import com.monglife.mongs.module.mqtt.utils.TopicUtil;
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

    @Autowired
    public MqttPublishAspect(MqttSendService mqttSendService) {
        this.mqttSendService = mqttSendService;
    }

    @Pointcut("execution(com.monglife.mongs.module.mqtt.dto.MqttResponseEntity *(..))")
    private void executionPointcut() {}

    @AfterReturning(value = "executionPointcut() && @annotation(mqttPublish)", returning = "mqttResponseEntity")
    public void afterReturning(JoinPoint joinPoint, MqttPublish mqttPublish, MqttResponseEntity<?> mqttResponseEntity) {

        String prefixTopic = TopicUtil.preProcessTopic(mqttPublish.value());

        for (String topic : mqttResponseEntity.getTopics()) {

            String sendTopic = String.format("%s/%s", prefixTopic, topic);

            mqttSendService.sendMessage(sendTopic, mqttResponseEntity.getBody());
        }
    }
}
