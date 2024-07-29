package com.mongs.play.core.error.module;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SecurityErrorCode implements ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SECURITY-100", "can't authorization"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "SECURITY-101", "forbidden error.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}