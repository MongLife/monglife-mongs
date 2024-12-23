package com.monglife.mongs.domain.taskSchedule.dto.etc;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StopTaskScheduleDto {

    private Long taskId;

    private String appPackageName;

    private String taskOwnerId;

    private String taskCode;

    private TaskStatusCode taskStatusCode;

    private Long restExpirationSeconds;

    private Long expirationSeconds;

    private LocalDateTime expiredAt;

    private Boolean isCycle;

    @Builder
    public StopTaskScheduleDto(Long taskId, String appPackageName, String taskOwnerId, String taskCode, TaskStatusCode taskStatusCode, Long restExpirationSeconds, Long expirationSeconds, LocalDateTime expiredAt, Boolean isCycle) {
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

    public static StopTaskScheduleDto of(TaskEntity taskEntity) {
        return StopTaskScheduleDto.builder()
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
