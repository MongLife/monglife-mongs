package com.monglife.mongs.domain.task.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@ToString
public class TaskStopEvent {

    private final String appCode;

    private final String taskOwnerId;

    private final String taskCode;

    private final Long expirationSeconds;

    @Builder
    public TaskStopEvent(String appCode, String taskOwnerId, String taskCode, Long expiration, LocalDateTime expiredAt) {
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.expirationSeconds = expiration - Duration.between(LocalDateTime.now(), expiredAt).getSeconds();
    }
}
