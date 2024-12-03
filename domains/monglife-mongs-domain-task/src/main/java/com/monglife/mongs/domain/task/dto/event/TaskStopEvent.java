package com.monglife.mongs.domain.task.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class TaskStopEvent {

    private final String appCode;

    private final String taskOwnerId;

    private final String taskCode;

    private final Long restExpirationSeconds;

    private final Long expirationSeconds;

    private final LocalDateTime expiredAt;

    @Builder
    public TaskStopEvent(String appCode, String taskOwnerId, String taskCode, Long restExpirationSeconds, Long expirationSeconds, LocalDateTime expiredAt) {
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.restExpirationSeconds = restExpirationSeconds;
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
    }
}
