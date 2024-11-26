package com.monglife.mongs.app.manager.global.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class MqttConfig {

    @Value("${application.client.mqtt.host}")
    private String HOST;

    @Value("${application.client.mqtt.port}")
    private Integer PORT;

    @Value("${application.client.mqtt.username}")
    private String USERNAME;

    @Value("${application.client.mqtt.password}")
    private String PASSWORD;

    @Value("${application.client.mqtt.base-topic}")
    private String BASE_TOPIC;

    /**
     * Mqtt Connect Configuration
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {

        MqttConnectOptions options = new MqttConnectOptions();
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        options.setAutomaticReconnect(true);

        options.setServerURIs(new String[]{ "tcp://" + HOST + ":" + PORT });
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);

        return factory;
    }

    /**
     * Mqtt Outbound Configuration
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(@Qualifier("mqttClientFactory") MqttPahoClientFactory mqttPahoClientFactory) {

        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory);

        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultTopic(BASE_TOPIC + "/error");

        return messageHandler;
    }
}
