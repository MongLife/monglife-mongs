package com.mongs.play.config;

import com.mongs.play.client.publisher.battle.client.MqttBattleInboundMatchClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class MqttBattleConfig {

    @Value("${application.mqtt.host}")
    private String HOST;
    @Value("${application.mqtt.port}")
    private Integer PORT;
    @Value("${application.mqtt.topic.mong_data}")
    private String TOPIC_FILTER;

    /**
     * Mqtt Connect Configuration
     */
    @Bean
    public MqttPahoClientFactory mqttBattleClientFactory() {
        final String BROKER_URL = "tcp://" + HOST + ":" + PORT;

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{BROKER_URL});
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
//        options.setUserName("username");
//        options.setPassword("password".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * Outbound Configuration
     */
    @Bean
    public MessageChannel mqttBattleOutboundChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttBattleOutboundChannel")
    public MessageHandler mqttBattleOutbound(@Qualifier("mqttBattleClientFactory") MqttPahoClientFactory mqttPahoClientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultTopic("mongs/fail");
        return messageHandler;
    }

    /**
     * Inbound Match Configuration
     */
    @Bean
    public MessageChannel mqttBattleInboundMatchChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageProducer mqttBattleInboundMatchMessageDrivenAdapter(
            @Qualifier("mqttBattleClientFactory") MqttPahoClientFactory mqttPahoClientFactory,
            @Qualifier("mqttBattleInboundMatchChannel") MessageChannel mqttBattleInboundMatchChannel
    ) {
        final String BROKER_URL = "tcp://" + HOST + ":" + PORT;
        final String subscribeTopic = TOPIC_FILTER + "battle/match";

        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(BROKER_URL, MqttAsyncClient.generateClientId(), mqttPahoClientFactory, subscribeTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttBattleInboundMatchChannel);
        return adapter;
    }
    @Bean
    @ServiceActivator(inputChannel = "mqttBattleInboundMatchChannel")
    public MessageHandler mqttBattleInboundMatch(@Autowired MqttBattleInboundMatchClient mqttBattleInboundMatchClient) {
        return mqttBattleInboundMatchClient;
    }
}
