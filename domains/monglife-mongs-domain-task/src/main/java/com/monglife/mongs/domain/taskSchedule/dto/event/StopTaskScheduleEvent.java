package com.monglife.mongs.domain.taskSchedule.dto.event;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StopTaskScheduleEvent {

    private final Long taskId;

    @Builder
    public StopTaskScheduleEvent(Long taskId) {
        this.taskId = taskId;
    }

    public static StopTaskScheduleEvent of(TaskEntity taskEntity) {
        return StopTaskScheduleEvent.builder()
                .taskId(taskEntity.getTaskId())
                .build();
    }
}
