package com.mongs.gateway.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GatewayErrorCode implements ErrorCode {
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "GATEWAY-100", "Not Found AccessToken"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "GATEWAY-101", "AccessToken Expired"),
    PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GATEWAY-102", "Passport Generate Fail"),
    CONNECT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GATEWAY-103", "Connect Micro Service Fail"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GATEWAY-104", "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
