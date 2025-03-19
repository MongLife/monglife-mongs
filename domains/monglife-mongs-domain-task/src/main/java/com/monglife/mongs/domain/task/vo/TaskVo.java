package com.monglife.mongs.domain.task.vo;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskVo {

    private final Long taskId;

    private final String appPackageName;

    private final String taskOwnerId;

    private final String taskCode;

    private final TaskStatusCode taskStatusCode;

    private final Long restExpirationSeconds;

    private final Long expirationSeconds;

    private final LocalDateTime expiredAt;

    private final Boolean isCycle;

    @Builder
    public TaskVo(Long taskId, String appPackageName, String taskOwnerId, String taskCode, TaskStatusCode taskStatusCode, Long restExpirationSeconds, Long expirationSeconds, LocalDateTime expiredAt, Boolean isCycle) {
        this.taskId = taskId;
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.taskStatusCode = taskStatusCode;
        this.restExpirationSeconds = restExpirationSeconds;
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
        this.isCycle = isCycle;
    }

    public static TaskVo of(TaskEntity taskEntity) {
        return TaskVo.builder()
                .taskId(taskEntity.getTaskId())
                .appPackageName(taskEntity.getAppPackageName())
                .taskOwnerId(taskEntity.getTaskOwnerId())
                .taskCode(taskEntity.getComn().getCode())
                .taskStatusCode(taskEntity.getTaskStatusCode())
                .restExpirationSeconds(taskEntity.getRestExpirationSeconds())
                .expirationSeconds(taskEntity.getExpirationSeconds())
                .expiredAt(taskEntity.getExpiredAt())
                .isCycle(taskEntity.isCycle())
                .build();
    }
}
