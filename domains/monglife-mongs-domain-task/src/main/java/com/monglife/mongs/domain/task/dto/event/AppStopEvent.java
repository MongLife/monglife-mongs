package com.monglife.mongs.domain.task.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AppStopEvent {

    private final String appCode;

    private final String taskOwnerId;

    private final String taskCode;

    private final Long restExpirationSeconds;

    private final Long expirationSeconds;

    @Builder
    public AppStopEvent(String appCode, String taskOwnerId, String taskCode, Long restExpirationSeconds, Long expirationSeconds) {
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.restExpirationSeconds = restExpirationSeconds;
        this.expirationSeconds = expirationSeconds;
    }
}
