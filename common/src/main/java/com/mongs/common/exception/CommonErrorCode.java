package com.mongs.common.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-100", "Internal Server Error"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-101", "Invalid Parameter"),
    NOT_FOUND_VERSION(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-102", "Not Found Version Information"),
    ALREADY_NEW_VERSION(HttpStatus.ACCEPTED, "COMMON-103", "Already Newest Version")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
