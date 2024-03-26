package com.mongs.core.interceptor.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FeignErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEIGN-100", "Internal Server Error"),
    GENERATE_PASSPORT_FAIL(HttpStatus.BAD_REQUEST, "FEIGN-101", "Generate Feign Passport Fail"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
