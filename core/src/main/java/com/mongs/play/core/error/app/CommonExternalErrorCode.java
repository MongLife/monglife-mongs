package com.mongs.play.core.error.app;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonExternalErrorCode implements ErrorCode {
    NOT_FOUND_VERSION(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_EXTERNAL-100", "not found version information.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
