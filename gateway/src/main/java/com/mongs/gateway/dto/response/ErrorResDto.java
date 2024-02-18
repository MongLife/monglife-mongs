package com.mongs.gateway.dto.response;

import com.mongs.gateway.exception.ErrorCode;
import lombok.Builder;

@Builder
public record ErrorResDto(
        String code,
        String message
) {
    public static ErrorResDto of(ErrorCode errorCode) {
        return ErrorResDto.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
}

