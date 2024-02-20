package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "AUTH-100", "Invalid Parameter"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-101", "RefreshToken Expired"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-102", "AccessToken Expired"),
    MEMBER_NOT_FOUND(HttpStatus.BAD_GATEWAY, "AUTH-103", "Not Found Member"),
    PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-104", "Passport Generate Fail");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
