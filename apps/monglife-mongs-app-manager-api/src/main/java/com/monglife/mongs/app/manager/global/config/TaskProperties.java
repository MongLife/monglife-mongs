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
public class TaskProperties {

    public SchedulerProperty sleep;

    public SchedulerProperty wakeup;

    public SchedulerProperty eggEvolution;

    public SchedulerProperty statusIncrease;

    public SchedulerProperty statusDecrease;

    public SchedulerProperty poopIncrease;

    @ToString
    @Getter
    @Setter
    public static class SchedulerProperty {

        private String code;

        private Long expiration;
    }
}
