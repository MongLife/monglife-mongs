package com.monglife.mongs.domain.task.dto.etc;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StartTaskScheduleDto {

    private Long taskId;

    private String appCode;

    private String taskOwnerId;

    private String taskCode;

    private LocalDateTime expiredAt;

    private Boolean isCycle;

    @Builder
    public StartTaskScheduleDto(Long taskId, String appCode, String taskOwnerId, String taskCode, LocalDateTime expiredAt, Boolean isCycle) {
        this.taskId = taskId;
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.expiredAt = expiredAt;
        this.isCycle = isCycle;
    }

    public static StartTaskScheduleDto of(TaskEntity taskEntity) {
        return StartTaskScheduleDto.builder()
                .taskId(taskEntity.getTaskId())
                .appCode(taskEntity.getAppCode())
                .taskOwnerId(taskEntity.getTaskOwnerId())
                .taskCode(taskEntity.getTaskCode().getComnCode())
                .expiredAt(taskEntity.getExpiredAt())
                .isCycle(taskEntity.getIsCycle())
                .build();
    }
}
