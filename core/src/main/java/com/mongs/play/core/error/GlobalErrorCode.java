package com.mongs.play.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR-000", "internal server error"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "ERROR-001", "invalid parameter"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
