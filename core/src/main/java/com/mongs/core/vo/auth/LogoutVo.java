package com.mongs.core.vo.auth;

import lombok.Builder;

@Builder
public record LogoutVo(
        Long accountId
) {
}
