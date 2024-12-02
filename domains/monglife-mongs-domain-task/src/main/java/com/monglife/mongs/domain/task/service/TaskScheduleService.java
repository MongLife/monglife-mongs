package com.monglife.mongs.domain.task.service;

import com.monglife.mongs.domain.task.dto.etc.CreateTaskScheduleDto;
import com.monglife.mongs.domain.task.dto.event.TaskRunEvent;
import com.monglife.mongs.domain.task.dto.event.TaskStopEvent;
import com.monglife.mongs.domain.task.entity.TaskScheduleEntity;
import com.monglife.mongs.domain.task.exception.NotExistsTaskScheduleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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


    @Transactional
    public void createTaskSchedule(CreateTaskScheduleDto createTaskScheduleDto) {

        TaskScheduleEntity taskScheduleEntity = TaskScheduleEntity.builder()
                .taskId(createTaskScheduleDto.getTaskId())
                .appCode(createTaskScheduleDto.getAppCode())
                .taskOwnerId(createTaskScheduleDto.getTaskOwnerId())
                .taskCode(createTaskScheduleDto.getTaskCode())
                .isCycle(createTaskScheduleDto.getIsCycle())
                .cycleSeconds(createTaskScheduleDto.getCycleSeconds())
                .build();

        long expirationSeconds = Duration.between(LocalDateTime.now(), createTaskScheduleDto.getExpiredAt()).getSeconds();

        // 시작
        taskScheduleEntity.init(
                taskScheduleEntity.getTaskStatusCode(),
                expirationSeconds,
                taskScheduleEntity.getExpiredAt(),
                this.executor.schedule(this.runTaskSchedule(taskScheduleEntity), expirationSeconds, TimeUnit.SECONDS));

        schedulerMap.put(taskScheduleEntity.getTaskId(), taskScheduleEntity);

        log.info("createTaskSchedule: {}", taskScheduleEntity);
    }

    @Transactional
    public void deleteTaskSchedule(Long taskId) {

        TaskScheduleEntity taskScheduleEntity = Optional.ofNullable(schedulerMap.get(taskId))
                .orElseThrow(() -> new NotExistsTaskScheduleException(taskId));

        taskScheduleEntity.delete();

        log.info("deleteTaskSchedule: {}", taskScheduleEntity);

        long expirationSeconds = Duration.between(LocalDateTime.now(), taskScheduleEntity.getExpiredAt()).getSeconds();

        applicationEventPublisher.publishEvent(TaskStopEvent.builder()
                .appCode(taskScheduleEntity.getAppCode())
                .taskOwnerId(taskScheduleEntity.getTaskOwnerId())
                .taskCode(taskScheduleEntity.getTaskCode())
                .expirationSeconds(expirationSeconds)
                .build());

        schedulerMap.remove(taskId);

    }

    private Runnable runTaskSchedule(TaskScheduleEntity taskScheduleEntity) {
        return () -> applicationEventPublisher.publishEvent(TaskRunEvent.builder()
                .appCode(taskScheduleEntity.getAppCode())
                .taskOwnerId(taskScheduleEntity.getTaskOwnerId())
                .taskCode(taskScheduleEntity.getTaskCode())
                .expirationSeconds(0L)
                .expiredAt(taskScheduleEntity.getExpiredAt())
                .build());
    }
}
