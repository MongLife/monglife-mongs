package com.mongs.auth.service.vo;

import lombok.Builder;

@Builder
public record LogoutVo(
        Long accountId
) {
}
