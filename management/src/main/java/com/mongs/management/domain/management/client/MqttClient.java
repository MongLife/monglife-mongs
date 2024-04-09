package com.mongs.management.domain.management.client;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttClient {
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);
}