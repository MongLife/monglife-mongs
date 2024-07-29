package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TaskErrorCode implements ErrorCode {
    NOT_FOUND_TASK_EVENT(HttpStatus.NOT_FOUND, "TASK-100", "not found task event."),
    NOT_FOUND_TASK(HttpStatus.NOT_FOUND, "TASK-101", "not found task."),
    ALREADY_EXIST_TASK(HttpStatus.NOT_ACCEPTABLE, "TASK-102", "already exist task."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
