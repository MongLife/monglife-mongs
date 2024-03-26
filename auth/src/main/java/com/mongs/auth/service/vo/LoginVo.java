package com.mongs.auth.service.vo;

import lombok.Builder;

@Builder
public record LoginVo(
        Long accountId,
        String accessToken,
        String refreshToken
) {
}
