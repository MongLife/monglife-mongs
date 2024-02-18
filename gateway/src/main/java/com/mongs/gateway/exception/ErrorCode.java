package com.mongs.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "100", "Not Found AccessToken"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "101", "AccessToken Expired"),
    PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "102", "Passport Generate Fail"),
    CONNECT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "103", "Connect Micro Service Fail"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "104", "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
