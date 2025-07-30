package com.monglife.mongs.adapter.in.battle.schedule.config;

import com.monglife.mongs.adapter.in.battle.schedule.worker.QueueScheduleWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class QueueScheduleConfig implements SchedulingConfigurer {

    private static final Long FIXED_DELAY = 1000L;

    private final ScheduledExecutorService scheduledExecutorService;

    private final QueueScheduleWorker queueScheduleWorker;

    public QueueScheduleConfig(
            @Autowired QueueScheduleWorker queueScheduleWorker,
            @Qualifier("battleScheduledExecutorService") ScheduledExecutorService scheduledExecutorService
    ) {
        this.queueScheduleWorker = queueScheduleWorker;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    /**
     * 매칭 스케줄
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.addFixedDelayTask(queueScheduleWorker::doMatching, Duration.ofMillis(FIXED_DELAY));
        registrar.setScheduler(scheduledExecutorService);
    }
}
