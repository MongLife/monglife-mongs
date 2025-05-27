package com.monglife.mongs.adapter.in.battle.schedule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@ComponentScan(basePackages = "com.monglife.mongs.adapter.in.battle.schedule")
public class AdapterInBattleScheduleConfig {

    @Value("${application.battle.scheduler.pool-size}")
    private Integer SCHEDULER_POOL_SIZE;

    @Bean(name = "battleScheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(SCHEDULER_POOL_SIZE);
    }
}
