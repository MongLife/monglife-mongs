package com.mongs.play.client.publisher.event.client;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "mqttEventOutboundChannel")
public interface MqttEventClient {
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);
}
