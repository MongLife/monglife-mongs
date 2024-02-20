package com.mongs.core.security.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SecurityErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SEC-000", "internal server error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SEC-001", "can't authorization"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "SEC-002", "can't authorization");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}