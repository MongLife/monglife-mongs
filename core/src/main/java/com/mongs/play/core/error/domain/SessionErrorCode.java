package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SessionErrorCode implements ErrorCode {
    NOT_FOUND_SESSION(HttpStatus.NOT_FOUND, "SESSION-100", "not found session.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
