package com.mongs.play.app.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ManagementInternalErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
