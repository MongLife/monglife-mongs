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
public class GetTaskDto {

    private Long taskId;

    private String appCode;

    private String taskOwnerId;

    private String taskCode;

    private TaskStatusCode taskStatusCode;

    private Long restExpirationSeconds;

    private Long expirationSeconds;

    private LocalDateTime expiredAt;

    private Boolean isCycle;

    @Builder
    public GetTaskDto(Long taskId, String appCode, String taskOwnerId, String taskCode, TaskStatusCode taskStatusCode, Long restExpirationSeconds, Long expirationSeconds, LocalDateTime expiredAt, Boolean isCycle) {
        this.taskId = taskId;
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.taskStatusCode = taskStatusCode;
        this.restExpirationSeconds = restExpirationSeconds;
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
        this.isCycle = isCycle;
    }

    public static GetTaskDto of(TaskEntity taskEntity) {
        return GetTaskDto.builder()
                .taskId(taskEntity.getTaskId())
                .appCode(taskEntity.getAppCode())
                .taskOwnerId(taskEntity.getTaskOwnerId())
                .taskCode(taskEntity.getTaskCode().getComnCode())
                .taskStatusCode(taskEntity.getTaskStatusCode())
                .restExpirationSeconds(taskEntity.getRestExpirationSeconds())
                .expirationSeconds(taskEntity.getExpirationSeconds())
                .expiredAt(taskEntity.getExpiredAt())
                .isCycle(taskEntity.getIsCycle())
                .build();
    }
}
