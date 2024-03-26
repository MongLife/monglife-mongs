package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-100", "Internal Server Error"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "AUTH-101", "Invalid Parameter"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.NOT_ACCEPTABLE, "AUTH-102", "RefreshToken Expired"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-103", "AccessToken Expired"),
    ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH-104", "Not Found Account"),
    ACCOUNT_LOG_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-105", "Not Found Account Log"),
    REGISTER_MEMBER_FAIL(HttpStatus.BAD_REQUEST, "AUTH-106", "Can't Make Member"),
    PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-107", "Passport Generate Fail"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
