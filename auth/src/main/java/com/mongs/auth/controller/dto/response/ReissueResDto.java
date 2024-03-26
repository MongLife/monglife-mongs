package com.mongs.auth.controller.dto.response;

import lombok.Builder;

@Builder
public record ReissueResDto(
        String accessToken,
        String refreshToken
) {
}