package com.mongs.play.core.dto.res;

import com.mongs.play.core.error.ErrorCode;
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

