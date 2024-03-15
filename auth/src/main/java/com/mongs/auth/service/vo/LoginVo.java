package com.mongs.auth.service.vo;

import lombok.Builder;

@Builder
public record LoginVo(
        String accessToken,
        String refreshToken
) {
}
