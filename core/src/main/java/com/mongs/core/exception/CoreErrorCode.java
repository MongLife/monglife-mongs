package com.mongs.core.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CoreErrorCode implements ErrorCode {
    GENERATE_PASSPORT_FAIL(HttpStatus.BAD_REQUEST, "FEIGN-100", "Generate Feign Passport Fail"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEIGN-101", "Internal Server Error")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
