package com.mongs.auth.controller.dto.response;

import lombok.Builder;

@Builder
public record LoginResDto(
        String accessToken,
        String refreshToken
) {
}