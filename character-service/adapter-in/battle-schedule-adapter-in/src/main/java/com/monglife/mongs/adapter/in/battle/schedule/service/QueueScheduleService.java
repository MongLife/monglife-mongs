package com.monglife.mongs.adapter.in.battle.schedule.service;

import com.monglife.mongs.application.battle.port.in.QueueUseCase;
import com.monglife.mongs.application.battle.port.in.command.MatchingQueuePlayersCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class QueueScheduleService implements SchedulingConfigurer {

    private static final Integer MATCH_PLAYER_COUNT = 2;
    private static final Long    FIXED_DELAY        = 1000L;

    private final ScheduledExecutorService scheduledExecutorService;

    private final QueueUseCase queueUseCase;

    public QueueScheduleService(
            @Autowired QueueUseCase queueUseCase,
            @Qualifier("battleScheduledExecutorService") ScheduledExecutorService scheduledExecutorService
    ) {
        this.queueUseCase = queueUseCase;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    /**
     * 매칭 스케줄
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.addFixedDelayTask(() -> {
            while (true) {
                try {
                    queueUseCase.matchingQueuePlayersUseCase(MatchingQueuePlayersCommand.builder()
                            .matchPlayerCount(MATCH_PLAYER_COUNT)
                            .build());
                } catch (Exception e) {
                    break;
                }
            }
        }, Duration.ofSeconds(FIXED_DELAY));

        registrar.setScheduler(scheduledExecutorService);
    }
}
