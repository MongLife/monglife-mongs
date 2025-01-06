package com.monglife.mongs.domain.taskSchedule.dto.event;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
public class StartTaskScheduleEvent {

    private final Long taskId;

    private final String appPackageName;

    private final String taskOwnerId;

    private final String taskCode;

    private final LocalDateTime expiredAt;

    private final Boolean isCycle;

    @Builder
    public StartTaskScheduleEvent(Long taskId, String appPackageName, String taskOwnerId, String taskCode, LocalDateTime expiredAt, Boolean isCycle) {
        this.taskId = taskId;
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.expiredAt = expiredAt;
        this.isCycle = isCycle;
    }

    public static StartTaskScheduleEvent of(TaskEntity taskEntity) {
        return StartTaskScheduleEvent.builder()
                .taskId(taskEntity.getTaskId())
                .appPackageName(taskEntity.getAppPackageName())
                .taskOwnerId(taskEntity.getTaskOwnerId())
                .taskCode(taskEntity.getComn().getCode())
                .expiredAt(taskEntity.getExpiredAt())
                .isCycle(taskEntity.isCycle())
                .build();
    }
}
