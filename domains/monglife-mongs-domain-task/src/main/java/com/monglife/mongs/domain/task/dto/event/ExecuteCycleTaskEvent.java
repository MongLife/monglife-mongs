package com.monglife.mongs.domain.task.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ExecuteCycleTaskEvent {

    private final String appPackageName;

    private final String taskOwnerId;

    private final String taskCode;

    private final LocalDateTime expiredAt;

    private final Long expirationSeconds;

    @Builder
    public ExecuteCycleTaskEvent(String appPackageName, String taskOwnerId, String taskCode, LocalDateTime expiredAt, Long expirationSeconds) {
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.expiredAt = expiredAt;
        this.expirationSeconds = expirationSeconds;
    }
}
