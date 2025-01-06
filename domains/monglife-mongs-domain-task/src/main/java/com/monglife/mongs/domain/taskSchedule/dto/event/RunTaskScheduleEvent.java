package com.monglife.mongs.domain.taskSchedule.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class RunTaskScheduleEvent {

    private final Long taskId;

    private final String appPackageName;

    private final String taskOwnerId;

    private final String taskCode;

    private final LocalDateTime expiredAt;

    private final Long expirationSeconds;

    private final Boolean isCycle;

    @Builder
    public RunTaskScheduleEvent(Long taskId, String appPackageName, String taskOwnerId, String taskCode, LocalDateTime expiredAt, Long expirationSeconds, Boolean isCycle) {
        this.taskId = taskId;
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.expiredAt = expiredAt;
        this.expirationSeconds = expirationSeconds;
        this.isCycle = isCycle;
    }
}
