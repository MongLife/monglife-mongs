package com.monglife.mongs.domain.task.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExecuteTaskEvent {

    private final String appPackageName;

    private final String taskOwnerId;

    private final String taskCode;

    private final Long restExpirationSeconds;

    private final Long expirationSeconds;

    @Builder
    public ExecuteTaskEvent(String appPackageName, String taskOwnerId, String taskCode, Long restExpirationSeconds, Long expirationSeconds) {
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.restExpirationSeconds = restExpirationSeconds;
        this.expirationSeconds = expirationSeconds;
    }
}
