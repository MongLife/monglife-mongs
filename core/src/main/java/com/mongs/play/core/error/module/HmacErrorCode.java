package com.mongs.play.core.error.module;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HmacErrorCode implements ErrorCode {
    GENERATE_HMAC(HttpStatus.INTERNAL_SERVER_ERROR, "HMAC-100", "generate hmac error.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
