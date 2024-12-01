package com.monglife.mongs.module.mqtt.consumer;

import com.monglife.mongs.module.mqtt.config.MqttMappingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
@RequiredArgsConstructor
@MessagingGateway(defaultRequestChannel = "mqttInboundChannel")
public class MqttConsumer implements MessageHandler {

    private final MqttMappingHandler mqttMappingHandler;


    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = (String) message.getPayload();

        mqttMappingHandler.invokeMappingMethod(topic == null ? "" : topic, payload);
    }
}
