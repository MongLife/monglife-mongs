package com.monglife.mongs.adapter.out.mong.schedule.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Getter
@ToString
public class TaskScheduleEntity {

    private final Long taskId;

    private final String appPackageName;

    private final String taskOwnerId;

    private final String schedulerTypeCode;

    private final Boolean isCycle;

    private final LocalDateTime expiredAt;

    private Long expirationSeconds;

    private ScheduledFuture<?> scheduler;

    @Builder
    private TaskScheduleEntity(Long taskId, String appPackageName, String taskOwnerId, String schedulerTypeCode, Boolean isCycle, LocalDateTime expiredAt) {
        this.taskId = taskId;
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.schedulerTypeCode = schedulerTypeCode;
        this.isCycle = isCycle;
        this.expiredAt = expiredAt;
    }

    public void start(ScheduledExecutorService executor, Runnable runnable) {
        this.expirationSeconds = Math.max(1, Duration.between(LocalDateTime.now(), this.expiredAt).getSeconds());
        this.scheduler = executor.schedule(runnable, this.expirationSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        this.scheduler.cancel(false);
    }

    public static TaskScheduleEntity of(TaskEntity taskEntity) {
        return TaskScheduleEntity.builder()
                .taskId(taskEntity.getTaskId())
                .appPackageName(taskEntity.getAppPackageName())
                .taskOwnerId(taskEntity.getTaskOwnerId())
                .schedulerTypeCode(taskEntity.getSchedulerTypeCode())
                .isCycle(taskEntity.isCycle())
                .expiredAt(taskEntity.getExpiredAt())
                .build();
    }
}
