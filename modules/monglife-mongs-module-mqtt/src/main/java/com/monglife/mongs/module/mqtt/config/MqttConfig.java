package com.monglife.mongs.module.mqtt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.mongs.module.mqtt.consumer.MqttConsumer;
import com.monglife.mongs.module.mqtt.utils.TopicUtil;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequiredArgsConstructor
public class MqttConfig {

    private final MqttConfigProperties mqttConfigProperties;

    /**
     * Mqtt ObjectMapper
     */
    @Bean("moduleMqttObjectMapper")
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

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        options.setAutomaticReconnect(true);

        options.setServerURIs(new String[]{ "tcp://" + mqttConfigProperties.host + ":" + mqttConfigProperties.port });
        options.setUserName(mqttConfigProperties.userName);
        options.setPassword(mqttConfigProperties.password.toCharArray());

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
        messageHandler.setDefaultQos(2);
        messageHandler.setDefaultTopic(TopicUtil.preProcessTopic(mqttConfigProperties.publisher.baseTopic) + "/error");

        return messageHandler;
    }

    /**
     * Mqtt Inbound Configuration
     */
    @Bean
    @ConditionalOnProperty(value = "module.mqtt.consumer.enabled", havingValue = "true", matchIfMissing = false)
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ConditionalOnProperty(value = "module.mqtt.consumer.enabled", havingValue = "true", matchIfMissing = false)
    public MessageProducer mqttInboundMessageDrivenAdapter(
            @Qualifier("mqttInboundChannel") MessageChannel mqttInboundChannel,
            @Qualifier("mqttClientFactory") MqttPahoClientFactory mqttPahoClientFactory
    ) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(MqttAsyncClient.generateClientId(), mqttPahoClientFactory);

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInboundChannel);

        // 구독 토픽 추가
        mqttConfigProperties.consumer.topics.forEach(adapter::addTopic);

        return adapter;
    }

    @Bean
    @ConditionalOnProperty(value = "module.mqtt.consumer.enabled", havingValue = "true", matchIfMissing = false)
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler mqttInbound(@Autowired MqttConsumer mqttConsumer) {
        return mqttConsumer;
    }
}
