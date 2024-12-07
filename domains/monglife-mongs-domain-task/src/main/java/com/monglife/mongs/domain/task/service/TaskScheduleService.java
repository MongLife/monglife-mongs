package com.monglife.mongs.domain.task.service;

import com.monglife.mongs.domain.task.dto.etc.GetTaskDto;
import com.monglife.mongs.domain.task.dto.etc.StartTaskScheduleDto;
import com.monglife.mongs.domain.task.dto.etc.StopTaskScheduleDto;
import com.monglife.mongs.domain.task.dto.event.AppStopEvent;
import com.monglife.mongs.domain.task.dto.event.ExecuteTaskEvent;
import com.monglife.mongs.domain.task.dto.event.RunTaskScheduleEvent;
import com.monglife.mongs.domain.task.entity.TaskScheduleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskScheduleService {

    private static final Map<Long, TaskScheduleEntity> schedulerMap = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ScheduledExecutorService executor;

    @Scheduled(fixedDelay = 1000)
    public void print() {
        StringBuffer sb = new StringBuffer();
        schedulerMap.keySet().forEach(key -> {
            TaskScheduleEntity taskScheduleEntity = schedulerMap.get(key);
            sb.append(taskScheduleEntity.getTaskOwnerId()).append(" : ").append(taskScheduleEntity.getTaskCode()).append("[").append(taskScheduleEntity.getExpiredAt()).append("]").append(", ");
        });
        log.info(sb.toString());
    }

    @Transactional
    public void startTaskSchedule(StartTaskScheduleDto startTaskScheduleDto) {

        TaskScheduleEntity taskScheduleEntity;

        if (!schedulerMap.containsKey(startTaskScheduleDto.getTaskId())) {
            taskScheduleEntity = TaskScheduleEntity.builder()
                    .taskId(startTaskScheduleDto.getTaskId())
                    .appCode(startTaskScheduleDto.getAppCode())
                    .taskOwnerId(startTaskScheduleDto.getTaskOwnerId())
                    .taskCode(startTaskScheduleDto.getTaskCode())
                    .isCycle(startTaskScheduleDto.getIsCycle())
                    .build();
        } else {
            taskScheduleEntity = schedulerMap.get(startTaskScheduleDto.getTaskId());
        }

        long expirationSeconds = Duration.between(LocalDateTime.now(), startTaskScheduleDto.getExpiredAt()).getSeconds();
        ScheduledFuture<?> timer =  this.executor.schedule(this.runTaskSchedule(taskScheduleEntity), expirationSeconds, TimeUnit.SECONDS);

        taskScheduleEntity.start(expirationSeconds, startTaskScheduleDto.getExpiredAt(), timer);

        schedulerMap.put(taskScheduleEntity.getTaskId(), taskScheduleEntity);
    }

    /**
     * Pause, AppStopPause, AppStopProcessing, Delete Task Entity
     * @param stopTaskScheduleDto Task ID
     */
    @Transactional
    public void stopTaskSchedule(StopTaskScheduleDto stopTaskScheduleDto) {

        Optional.ofNullable(schedulerMap.get(stopTaskScheduleDto.getTaskId()))
                .ifPresent(taskScheduleEntity -> {
                    taskScheduleEntity.stop();
                    schedulerMap.remove(stopTaskScheduleDto.getTaskId());
                });

        switch (stopTaskScheduleDto.getTaskStatusCode()) {
            case PAUSE -> {
                applicationEventPublisher.publishEvent(ExecuteTaskEvent.builder()
                        .appCode(stopTaskScheduleDto.getAppCode())
                        .taskOwnerId(stopTaskScheduleDto.getTaskOwnerId())
                        .taskCode(stopTaskScheduleDto.getTaskCode())
                        .restExpirationSeconds(stopTaskScheduleDto.getRestExpirationSeconds())
                        .expirationSeconds(stopTaskScheduleDto.getExpirationSeconds())
                        .build());

                log.info("[PAUSE STOP] {} -> {}", stopTaskScheduleDto.getTaskOwnerId(), stopTaskScheduleDto.getTaskCode());
            }
            case APP_STOP_PROCESSING -> {
                applicationEventPublisher.publishEvent(AppStopEvent.builder()
                        .appCode(stopTaskScheduleDto.getAppCode())
                        .taskOwnerId(stopTaskScheduleDto.getTaskOwnerId())
                        .taskCode(stopTaskScheduleDto.getTaskCode())
                        .restExpirationSeconds(stopTaskScheduleDto.getRestExpirationSeconds())
                        .expirationSeconds(stopTaskScheduleDto.getExpirationSeconds())
                        .build());

                log.info("[APP STOP] {} -> {}", stopTaskScheduleDto.getTaskOwnerId(), stopTaskScheduleDto.getTaskCode());
            }
        }
    }

    /**
     * Task Scheduler Run
     * @param taskScheduleEntity Task Scheduler Entity
     * @return Runnable
     */

    private Runnable runTaskSchedule(TaskScheduleEntity taskScheduleEntity) {
        return () -> applicationEventPublisher.publishEvent(RunTaskScheduleEvent.builder()
                .taskId(taskScheduleEntity.getTaskId())
                .build());
    }
}
