package com.mongs.core.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityErrorCode {
    INTERNAL_SERVER_ERROR("000", "internal server error"),
    UNAUTHORIZATION("001", "can't authorization"),
    FORBIDDEN("002", "can't authorization");

    private final String code;
    private final String message;
}