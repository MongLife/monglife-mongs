package com.monglife.mongs.domain.task.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.task.enums.TaskResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsTaskCodeException extends ErrorException {

    public NotExistsTaskCodeException(String taskCode) {
        this.response = TaskResponse.DOMAIN_TASK_NOT_EXISTS_TASK_CODE;
        this.result = Collections.singletonMap("taskCode", taskCode);
    }
}
