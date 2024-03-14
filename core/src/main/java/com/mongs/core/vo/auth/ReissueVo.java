package com.mongs.core.vo.auth;

import lombok.Builder;

@Builder
public record ReissueVo(
        String accessToken,
        String refreshToken
) {
}
