package com.monglife.mongs.domain.task.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.task.enums.TaskResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class NotExistsTaskException extends ErrorException {

    public NotExistsTaskException(Long taskId) {
        this.response = TaskResponse.DOMAIN_TASK_NOT_EXISTS_TASK;
        this.result = Collections.singletonMap("taskId", taskId);
    }
}
