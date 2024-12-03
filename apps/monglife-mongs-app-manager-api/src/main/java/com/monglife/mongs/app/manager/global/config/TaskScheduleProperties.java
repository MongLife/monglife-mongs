package com.monglife.mongs.app.manager.global.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Setter
@Component
@ConfigurationProperties(prefix = "application.scheduler.task")
public class TaskScheduleProperties {

    public SchedulerProperty eggEvolution;

    public SchedulerProperty sleep;

    public SchedulerProperty wakeup;

    public SchedulerProperty statusIncrease;

    public SchedulerProperty statusDecrease;

    public SchedulerProperty poopIncrease;

    public SchedulerProperty dead;

    @ToString
    @Getter
    @Setter
    public static class SchedulerProperty {

        private String code;

        private Long expiration;

        private Double exp;

        private Double weight;

        private Double strengthRatio;

        private Double satietyRatio;

        private Double healthyRatio;

        private Double fatigueRatio;

        private Integer poop;
    }
}
