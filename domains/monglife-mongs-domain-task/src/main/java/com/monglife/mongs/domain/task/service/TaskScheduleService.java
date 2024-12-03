package com.monglife.mongs.domain.task.service;

import com.monglife.mongs.domain.task.dto.etc.CreateTaskScheduleDto;
import com.monglife.mongs.domain.task.dto.event.TaskRunEvent;
import com.monglife.mongs.domain.task.dto.event.TaskStopEvent;
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
    public void startTaskSchedule(CreateTaskScheduleDto createTaskScheduleDto) {

        TaskScheduleEntity taskScheduleEntity = TaskScheduleEntity.builder()
                .taskId(createTaskScheduleDto.getTaskId())
                .appCode(createTaskScheduleDto.getAppCode())
                .taskOwnerId(createTaskScheduleDto.getTaskOwnerId())
                .taskCode(createTaskScheduleDto.getTaskCode())
                .isCycle(createTaskScheduleDto.getIsCycle())
                .cycleSeconds(createTaskScheduleDto.getCycleSeconds())
                .build();

        if (!schedulerMap.containsKey(taskScheduleEntity.getTaskId())) {

            long expirationSeconds = Duration.between(LocalDateTime.now(), createTaskScheduleDto.getExpiredAt()).getSeconds();

            // 시작
            taskScheduleEntity.start(
                    taskScheduleEntity.getTaskStatusCode(),
                    expirationSeconds,
                    taskScheduleEntity.getExpiredAt(),
                    this.executor.schedule(this.runTaskSchedule(taskScheduleEntity), expirationSeconds, TimeUnit.SECONDS));

            schedulerMap.put(taskScheduleEntity.getTaskId(), taskScheduleEntity);
        }
    }

    @Transactional
    public void deleteTaskSchedule(Long taskId) {

        Optional.ofNullable(schedulerMap.get(taskId))
                .ifPresent(taskScheduleEntity -> {

                    taskScheduleEntity.stop();

                    schedulerMap.remove(taskId);

                    long restExpirationSeconds = Duration.between(LocalDateTime.now(), taskScheduleEntity.getExpiredAt()).getSeconds();

                    applicationEventPublisher.publishEvent(TaskStopEvent.builder()
                            .appCode(taskScheduleEntity.getAppCode())
                            .taskOwnerId(taskScheduleEntity.getTaskOwnerId())
                            .taskCode(taskScheduleEntity.getTaskCode())
                            .restExpirationSeconds(restExpirationSeconds)
                            .expirationSeconds(taskScheduleEntity.getExpirationSeconds())
                            .build());
                });
    }

    private Runnable runTaskSchedule(TaskScheduleEntity taskScheduleEntity) {
        return () -> {

            schedulerMap.remove(taskScheduleEntity.getTaskId());

            if (taskScheduleEntity.getIsCycle()) {
                // 시작
                taskScheduleEntity.start(
                        taskScheduleEntity.getTaskStatusCode(),
                        taskScheduleEntity.getCycleSeconds(),
                        LocalDateTime.now().plusSeconds(taskScheduleEntity.getCycleSeconds()),
                        this.executor.schedule(this.runTaskSchedule(taskScheduleEntity), taskScheduleEntity.getCycleSeconds(), TimeUnit.SECONDS));

                schedulerMap.put(taskScheduleEntity.getTaskId(), taskScheduleEntity);
            }

            applicationEventPublisher.publishEvent(TaskRunEvent.builder()
                    .appCode(taskScheduleEntity.getAppCode())
                    .taskOwnerId(taskScheduleEntity.getTaskOwnerId())
                    .taskCode(taskScheduleEntity.getTaskCode())
                    .build());
        };
    }
}
