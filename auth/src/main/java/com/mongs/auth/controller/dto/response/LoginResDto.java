package com.mongs.auth.controller.dto.response;

import lombok.Builder;

@Builder
public record LoginResDto(
        Long accountId,
        String accessToken,
        String refreshToken
) {
}
