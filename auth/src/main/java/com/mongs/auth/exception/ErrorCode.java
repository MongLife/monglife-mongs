package com.mongs.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_PARAMETER("ERROR_100", "Invalid Parameter");
    
    private final String code;
    private final String message;
}
