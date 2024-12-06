package com.monglife.mongs.domain.task.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class StopTaskScheduleEvent {

    private final Long taskId;

    @Builder
    public StopTaskScheduleEvent(Long taskId) {
        this.taskId = taskId;
    }
}
