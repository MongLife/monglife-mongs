package com.monglife.mongs.domain.task.dto.etc;

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
public class CreateTaskScheduleDto {

    private Long taskId;

    private String appCode;

    private String taskOwnerId;

    private String taskCode;

    private TaskStatusCode taskStatusCode;

    private Long expirationSeconds;

    private LocalDateTime expiredAt;

    private Boolean isCycle;

    private Long cycleSeconds;

    @Builder
    public CreateTaskScheduleDto(Long taskId, String appCode, String taskOwnerId, String taskCode, TaskStatusCode taskStatusCode, Long expirationSeconds, LocalDateTime expiredAt, Boolean isCycle, Long cycleSeconds) {
        this.taskId = taskId;
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.taskStatusCode = taskStatusCode;
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
        this.isCycle = isCycle;
        this.cycleSeconds = cycleSeconds;
    }

    public static CreateTaskScheduleDto of(TaskEntity taskEntity) {
        return CreateTaskScheduleDto.builder()
                .taskId(taskEntity.getTaskId())
                .appCode(taskEntity.getAppCode())
                .taskOwnerId(taskEntity.getTaskOwnerId())
                .taskCode(taskEntity.getTaskCode().getComnCode())
                .taskStatusCode(taskEntity.getTaskStatusCode())
                .expirationSeconds(taskEntity.getExpirationSeconds())
                .expiredAt(taskEntity.getExpiredAt())
                .isCycle(taskEntity.getIsCycle())
                .cycleSeconds(taskEntity.getCycleSeconds())
                .build();
    }
}
