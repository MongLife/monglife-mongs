package com.monglife.mongs.adapter.out.mong.schedule.service;

import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskScheduleEntity;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskScheduleRepository;
import com.monglife.mongs.adapter.transaction.IncreaseStatusEventDto;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskScheduleService {

    private final KafkaService kafkaService;

    private final TaskScheduleRepository taskScheduleRepository;

    private final ScheduledExecutorService executor;

    public void startTaskSchedule(TaskEntity taskEntity) {

        TaskScheduleEntity taskScheduleEntity = taskScheduleRepository.findByTaskId(taskEntity.getTaskId())
                .orElse(TaskScheduleEntity.builder()
                        .taskId(taskEntity.getTaskId())
                        .appPackageName(taskEntity.getAppPackageName())
                        .taskOwnerId(taskEntity.getTaskOwnerId())
                        .schedulerTypeCode(taskEntity.getSchedulerTypeCode())
                        .isCycle(taskEntity.isCycle())
                        .build());

        long expirationSeconds = Duration.between(LocalDateTime.now(), taskEntity.getExpiredAt()).getSeconds();
        ScheduledFuture<?> timer =  this.executor.schedule(this.runTaskSchedule(taskScheduleEntity), expirationSeconds, TimeUnit.SECONDS);

        taskScheduleEntity.start(expirationSeconds, taskEntity.getExpiredAt(), timer);

        taskScheduleRepository.save(taskScheduleEntity);
    }

    /**
     * Pause, AppStopPause, AppStopProcessing, Delete Task Entity
     * @param taskId Task ID
     */
    public void stopTaskSchedule(Long taskId) {
        taskScheduleRepository.stopAndDeleteByTaskId(taskId);
    }

    /**
     * 몽 스케줄 실행 이벤트
     * @param taskScheduleEntity Task Scheduler Entity
     * @return Runnable
     */
    private Runnable runTaskSchedule(TaskScheduleEntity taskScheduleEntity) {
        return switch (taskScheduleEntity.getSchedulerTypeCode()) {
            case EGG_EVOLUTION -> () -> kafkaService.generateEvent("commit.eggEvolution", IncreaseStatusEventDto.builder()
                    .taskId(taskScheduleEntity.getTaskId())
                    .build());
            case INCREASE_STATUS -> () -> kafkaService.generateEvent("commit.increaseStatus", IncreaseStatusEventDto.builder()
                    .taskId(taskScheduleEntity.getTaskId())
                    .build());
            case DECREASE_STATUS -> () -> kafkaService.generateEvent("commit.decreaseStatus", IncreaseStatusEventDto.builder()
                    .taskId(taskScheduleEntity.getTaskId())
                    .build());
            case INCREASE_POOP -> () -> kafkaService.generateEvent("commit.increasePoop", IncreaseStatusEventDto.builder()
                    .taskId(taskScheduleEntity.getTaskId())
                    .build());
            case DEAD -> () -> kafkaService.generateEvent("commit.dead", IncreaseStatusEventDto.builder()
                    .taskId(taskScheduleEntity.getTaskId())
                    .build());
            case SLEEP -> () -> kafkaService.generateEvent("commit.sleep", IncreaseStatusEventDto.builder()
                    .taskId(taskScheduleEntity.getTaskId())
                    .build());
            case WAKEUP -> () -> kafkaService.generateEvent("commit.wakeup", IncreaseStatusEventDto.builder()
                    .taskId(taskScheduleEntity.getTaskId())
                    .build());
        };
    }
}
