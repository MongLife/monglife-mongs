package com.mongs.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_PARAMETER("100", "Invalid Parameter"),
    REFRESH_TOKEN_EXPIRED("101", "RefreshToken Expired");
    
    private final String code;
    private final String message;
}
