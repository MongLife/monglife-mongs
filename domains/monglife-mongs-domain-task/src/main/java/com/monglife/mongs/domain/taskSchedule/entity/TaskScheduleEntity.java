package com.monglife.mongs.domain.taskSchedule.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

@Getter
public class TaskScheduleEntity {

    private final Long taskId;

    private final String appPackageName;

    private final String taskOwnerId;

    private final String taskCode;

    private final Boolean isCycle;

    private Long expirationSeconds;

    private LocalDateTime expiredAt;

    private ScheduledFuture<?> scheduler;

    @Builder
    public TaskScheduleEntity(Long taskId, String appPackageName, String taskOwnerId, String taskCode, Boolean isCycle) {
        this.taskId = taskId;
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.isCycle = isCycle;
    }

    public void start(Long expirationSeconds, LocalDateTime expiredAt, ScheduledFuture<?> scheduler) {
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
        this.scheduler = scheduler;
    }

    public void stop() {
        this.scheduler.cancel(false);
    }
}
