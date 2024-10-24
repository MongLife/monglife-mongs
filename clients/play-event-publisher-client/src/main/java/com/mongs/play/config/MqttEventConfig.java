package com.mongs.play.config;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
public class MqttEventConfig {

    @Value("${application.mqtt.host}")
    private String HOST;
    @Value("${application.mqtt.port}")
    private Integer PORT;
    @Value("${application.mqtt.username}")
    private String USERNAME;
    @Value("${application.mqtt.password}")
    private String PASSWORD;

    @Bean
    public MqttPahoClientFactory mqttEventClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://" + HOST + ":" + PORT});
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }
    @Bean
    public MessageChannel mqttEventOutboundChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttEventOutboundChannel")
    public MessageHandler mqttEventOutbound(@Qualifier("mqttEventClientFactory") MqttPahoClientFactory mqttPahoClientFactory) {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("mongs/fail");
        return messageHandler;
    }
}
