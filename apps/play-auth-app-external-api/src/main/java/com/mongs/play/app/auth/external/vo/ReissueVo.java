package com.mongs.play.app.auth.external.vo;

import lombok.Builder;

@Builder
public record ReissueVo(
        String accessToken,
        String refreshToken
) {
}
