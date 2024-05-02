package com.mongs.play.core.error.app;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GatewayExternalErrorCode implements ErrorCode {
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "GATEWAY-100", "not found access token."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "GATEWAY-101", "access token expired."),
    PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GATEWAY-102", "passport generate fail."),
    PASSPORT_PARSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GATEWAY-103", "passport generate fail."),
    CONNECT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GATEWAY-104", "connect service fail.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
