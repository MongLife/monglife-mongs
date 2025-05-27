package com.monglife.mongs.adapter.out.mong.schedule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@ComponentScan({ "com.monglife.mongs.adapter.out.mong.schedule", "com.monglife.module" })
public class AdapterOutMongScheduleConfig {

    @Value("${application.task.scheduler.pool-size}")
    private Integer SCHEDULER_POOL_SIZE;

    @Bean(name = "taskScheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(SCHEDULER_POOL_SIZE);
    }
}
