package com.monglife.mongs.domain.task.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.task.enums.TaskResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyExistsTaskException extends ErrorException {

    public AlreadyExistsTaskException(Long taskId) {
        this.response = TaskResponse.DOMAIN_TASK_ALREADY_EXISTS_TASK;
        this.result = Collections.singletonMap("taskId", taskId);
    }
}
