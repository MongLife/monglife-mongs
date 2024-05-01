package com.mongs.play.app.auth.external.vo;

import lombok.Builder;

@Builder
public record LoginVo(
        Long accountId,
        String accessToken,
        String refreshToken
) {
}
