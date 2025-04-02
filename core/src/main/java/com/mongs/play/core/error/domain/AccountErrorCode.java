package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AccountErrorCode implements ErrorCode {
    NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, "ACCOUNT-100", "not found account.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
