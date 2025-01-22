package com.monglife.mongs.module.mqtt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Setter
@Component
@ConfigurationProperties(prefix = "module.mqtt")
public class MqttConfigProperties {

    public String host = "127.0.0.1";

    public Integer port = 1883;

    public String userName = "";

    public String password = "";

    public Consumer consumer = new Consumer();

    public Publisher publisher = new Publisher();


    @Getter
    @Setter
    public static class Consumer {

        public Boolean enabled = false;

        public List<String> topics = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Publisher {

        public String baseTopic = "topic";
    }
}
