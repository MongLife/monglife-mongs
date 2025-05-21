package com.monglife.mongs.adapter.out.mong.schedule.entity;

import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

@Getter
public class TaskScheduleEntity {

    private final Long taskId;

    private final String appPackageName;

    private final String taskOwnerId;

    private final MongSchedulerTypeCode schedulerTypeCode;

    private final Boolean isCycle;

    private Long expirationSeconds;

    private LocalDateTime expiredAt;

    private ScheduledFuture<?> scheduler;

    @Builder
    public TaskScheduleEntity(Long taskId, String appPackageName, String taskOwnerId, MongSchedulerTypeCode schedulerTypeCode, Boolean isCycle) {
        this.taskId = taskId;
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.schedulerTypeCode = schedulerTypeCode;
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
