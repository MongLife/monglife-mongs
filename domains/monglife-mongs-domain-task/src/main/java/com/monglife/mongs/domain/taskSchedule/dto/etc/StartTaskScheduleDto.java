package com.monglife.mongs.domain.taskSchedule.dto.etc;

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

    private String appPackageName;

    private String taskOwnerId;

    private String taskCode;

    private LocalDateTime expiredAt;

    private Boolean isCycle;

    @Builder
    public StartTaskScheduleDto(Long taskId, String appPackageName, String taskOwnerId, String taskCode, LocalDateTime expiredAt, Boolean isCycle) {
        this.taskId = taskId;
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.expiredAt = expiredAt;
        this.isCycle = isCycle;
    }
}
