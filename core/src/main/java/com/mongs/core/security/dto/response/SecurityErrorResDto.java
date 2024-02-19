package com.mongs.core.security.dto.response;

import lombok.Builder;

@Builder
public record SecurityErrorResDto(
        String code,
        String message
) {
    public static SecurityErrorResDto of (SecurityErrorCode securityErrorCode) {
        return SecurityErrorResDto.builder()
                .code(securityErrorCode.getCode())
                .message(securityErrorCode.getMessage())
                .build();
    }
}