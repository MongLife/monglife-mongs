package com.monglife.mongs.app.activity.training.config;

import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Setter
@Component
@ConfigurationProperties(prefix = "application.service.training")
public class TrainingProperties {

    public Property runner;

    @Setter
    public static class Property {

        public Double exp;

        public Double weight;

        public Double strength;

        public Double satiety;

        public Double healthy;

        public Double fatigue;

        public Integer poopCount;

        public Integer rewardPayPoint;
    }
}
