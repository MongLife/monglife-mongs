package com.monglife.mongs.domain.taskSchedule.service;

import com.monglife.mongs.domain.taskSchedule.dto.etc.StartTaskScheduleDto;
import com.monglife.mongs.domain.taskSchedule.dto.event.RunTaskScheduleEvent;
import com.monglife.mongs.domain.taskSchedule.entity.TaskScheduleEntity;
import com.monglife.mongs.domain.taskSchedule.repository.TaskScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

    private final TaskScheduleRepository taskScheduleRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ScheduledExecutorService executor;

    public void startTaskSchedule(StartTaskScheduleDto startTaskScheduleDto) {

        TaskScheduleEntity taskScheduleEntity = taskScheduleRepository.findByTaskId(startTaskScheduleDto.getTaskId())
                .orElse(TaskScheduleEntity.builder()
                        .taskId(startTaskScheduleDto.getTaskId())
                        .appPackageName(startTaskScheduleDto.getAppPackageName())
                        .taskOwnerId(startTaskScheduleDto.getTaskOwnerId())
                        .taskCode(startTaskScheduleDto.getTaskCode())
                        .isCycle(startTaskScheduleDto.getIsCycle())
                        .build());

        long expirationSeconds = Duration.between(LocalDateTime.now(), startTaskScheduleDto.getExpiredAt()).getSeconds();
        ScheduledFuture<?> timer =  this.executor.schedule(this.runTaskSchedule(taskScheduleEntity), expirationSeconds, TimeUnit.SECONDS);

        taskScheduleEntity.start(expirationSeconds, startTaskScheduleDto.getExpiredAt(), timer);

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
     * Task Scheduler Run
     * @param taskScheduleEntity Task Scheduler Entity
     * @return Runnable
     */
    private Runnable runTaskSchedule(TaskScheduleEntity taskScheduleEntity) {
        return () -> applicationEventPublisher.publishEvent(RunTaskScheduleEvent.builder()
                .taskId(taskScheduleEntity.getTaskId())
                .appPackageName(taskScheduleEntity.getAppPackageName())
                .taskOwnerId(taskScheduleEntity.getTaskOwnerId())
                .taskCode(taskScheduleEntity.getTaskCode())
                .expiredAt(taskScheduleEntity.getExpiredAt())
                .expirationSeconds(taskScheduleEntity.getExpirationSeconds())
                .isCycle(taskScheduleEntity.getIsCycle())
                .build());
    }
}
