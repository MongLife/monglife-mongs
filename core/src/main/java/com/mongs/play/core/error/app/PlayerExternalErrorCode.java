package com.mongs.play.core.error.app;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PlayerExternalErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
