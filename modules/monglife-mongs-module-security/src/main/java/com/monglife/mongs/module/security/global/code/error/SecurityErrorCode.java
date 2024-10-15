package com.monglife.mongs.module.security.global.code.error;

import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SecurityErrorCode implements ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "SECURITY-100", "can't authorization."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "SECURITY-101", "forbidden error.");

    private final Integer httpStatus;
    private final String code;
    private final String message;
}