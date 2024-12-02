package com.monglife.mongs.domain.task.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class TaskRunEvent {

    private final String appCode;

    private final String taskOwnerId;

    private final String taskCode;

    private final Long expirationSeconds;

    private final LocalDateTime expiredAt;

    @Builder
    public TaskRunEvent(String appCode, String taskOwnerId, String taskCode, Long expirationSeconds, LocalDateTime expiredAt) {
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
    }
}
