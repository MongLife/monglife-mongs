package com.mongs.lifecycle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class SchedulerConfig {
    @Value("${application.scheduler.pool-size}")
    private Integer SINGLE_POOL_SIZE;

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(SINGLE_POOL_SIZE);
    }
}
