package com.mongs.play.client.publisher.battle.client;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "mqttBattleOutboundChannel")
public interface MqttBattleOutboundClient {
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);
}
