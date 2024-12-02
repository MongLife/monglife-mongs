package com.monglife.mongs.domain.task.entity;

import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

@Getter
@ToString
public class TaskScheduleEntity {

    private final Long taskId;

    private final String appCode;

    private final String taskOwnerId;

    private final String taskCode;

    private final Boolean isCycle;

    private final Long cycleSeconds;

    @Setter
    private TaskStatusCode taskStatusCode;

    @Setter
    private Long expirationSeconds;

    @Setter
    private LocalDateTime expiredAt;

    @Setter
    private ScheduledFuture<?> scheduler;

    @Builder
    public TaskScheduleEntity(Long taskId, String appCode, String taskOwnerId, String taskCode, Boolean isCycle, Long cycleSeconds) {
        this.taskId = taskId;
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.isCycle = isCycle;
        this.cycleSeconds = cycleSeconds;
    }

    public void init(TaskStatusCode taskStatusCode, Long expirationSeconds, LocalDateTime expiredAt, ScheduledFuture<?> scheduler) {
        this.taskStatusCode = taskStatusCode;
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
        this.scheduler = scheduler;
    }

    public void delete() {
        this.scheduler.cancel(false);
    }
}
