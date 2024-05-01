package com.mongs.play.app.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthExternalErrorCode implements ErrorCode {
    REFRESH_TOKEN_EXPIRED(HttpStatus.NOT_ACCEPTABLE, "AUTH_EXTERNAL-100", "refresh token expired."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_EXTERNAL-101", "access token expired."),
    ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_EXTERNAL-102", "not found account."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
