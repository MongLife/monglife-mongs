package com.mongs.core.vo.auth;

import lombok.Builder;

@Builder
public record LoginVo(
        String accessToken,
        String refreshToken
) {
}
