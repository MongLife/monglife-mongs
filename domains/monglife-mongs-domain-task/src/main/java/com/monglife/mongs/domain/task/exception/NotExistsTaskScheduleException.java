package com.monglife.mongs.domain.task.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.task.enums.TaskResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class NotExistsTaskScheduleException extends ErrorException {

    public NotExistsTaskScheduleException(Long taskId) {
        this.response = TaskResponse.DOMAIN_TASK_NOT_EXISTS_TASK_SCHEDULE;
        this.result = Map.of("taskId", taskId);
    }
}
