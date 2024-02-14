package com.mongs.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_PARAMETER("Invalid Parameter"),
    REFRESH_TOKEN_EXPIRED("RefreshToken Expired"),
    ACCESS_TOKEN_EXPIRED("AccessToken Expired"),
    MEMBER_NOT_FOUND("Not Found Member"),
    PASSPORT_GENERATE_FAIL("Passport Generate Fail");

    private final String message;
}
