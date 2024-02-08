package com.mongs.auth.dto.response;

import lombok.Builder;

@Builder
public record ReissueResDto(
        String accessToken,
        String refreshToken
) {
}