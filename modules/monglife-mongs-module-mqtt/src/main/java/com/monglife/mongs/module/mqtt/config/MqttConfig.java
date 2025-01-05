package com.monglife.mongs.module.mqtt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.mongs.module.mqtt.consumer.MqttConsumer;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

@Configuration
public class MqttConfig {

    @Value("${spring.mqtt.host}")
    private String HOST;

    @Value("${spring.mqtt.port}")
    private Integer PORT;

    @Value("${spring.mqtt.username}")
    private String USERNAME;

    @Value("${spring.mqtt.password}")
    private String PASSWORD;

    @Value("${spring.mqtt.base-topic}")
    private String BASE_TOPIC;

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

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

    /**
     * Mqtt Inbound Configuration
     */
    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.mqtt.consume", havingValue = "true")
    public MessageProducer mqttInboundMessageDrivenAdapter(
            @Qualifier("mqttInboundChannel") MessageChannel mqttInboundChannel,
            @Qualifier("mqttClientFactory") MqttPahoClientFactory mqttPahoClientFactory
    ) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(MqttAsyncClient.generateClientId(), mqttPahoClientFactory, BASE_TOPIC + "/#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel);

        return adapter;
    }

    @Bean
    @ConditionalOnProperty(value = "spring.mqtt.consume", havingValue = "true")
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler mqttInbound(@Autowired MqttConsumer mqttConsumer) {
        return mqttConsumer;
    }
}
