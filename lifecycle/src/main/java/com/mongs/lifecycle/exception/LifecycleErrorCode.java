package com.mongs.lifecycle.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LifecycleErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "LIFECYCLE-100", "Internal Server Error"),
    NOT_FOUND_TASK(HttpStatus.INTERNAL_SERVER_ERROR, "LIFECYCLE-101", "Not Found Task"),
    NOT_FOUND_MONG(HttpStatus.INTERNAL_SERVER_ERROR, "LIFECYCLE-102", "Not Found Mong"),
    GENERATE_TASK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "LIFECYCLE-103", "Generate Task Fail"),
    EXIST_TASK(HttpStatus.INTERNAL_SERVER_ERROR, "LIFECYCLE-104", "Exist Task"),

    CONNECT_REFUSE(HttpStatus.INTERNAL_SERVER_ERROR, "MANAGEMENT-104", "Mqtt Connect Fail"),
    GENERATE_DATA_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MANAGEMENT-105", "Generate Data Fail (Json Paring Fail)")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
