package com.monglife.mongs.domain.task.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.task.enums.TaskResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class NotExistsTaskException extends ErrorException {

    public NotExistsTaskException(String appCode, String taskOwnerId, String taskCode) {
        this.response = TaskResponse.DOMAIN_TASK_NOT_EXISTS_TASK_CODE;
        this.result = Map.of("appCode", appCode, "taskOwnerId", taskOwnerId, "taskCode", taskCode);
    }
}
