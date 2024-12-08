package com.monglife.mongs.domain.taskSchedule.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RunTaskScheduleEvent {

    private final Long taskId;

    @Builder
    public RunTaskScheduleEvent(Long taskId) {
        this.taskId = taskId;
    }
}
