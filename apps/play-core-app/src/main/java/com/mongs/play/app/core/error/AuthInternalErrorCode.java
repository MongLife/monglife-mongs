package com.mongs.play.app.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthInternalErrorCode implements ErrorCode {
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_INTERNAL-100", "access token expired."),
    ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_INTERNAL-101", "not found account."),
    PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_INTERNAL-102", "passport generate fail."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
