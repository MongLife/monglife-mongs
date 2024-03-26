package com.mongs.auth.service.vo;

import lombok.Builder;

@Builder
public record ReissueVo(
        String accessToken,
        String refreshToken
) {
}
