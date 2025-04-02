package com.mongs.play.app.auth.vo;

import lombok.Builder;

@Builder
public record LogoutVo(
        Long accountId
) {
}
