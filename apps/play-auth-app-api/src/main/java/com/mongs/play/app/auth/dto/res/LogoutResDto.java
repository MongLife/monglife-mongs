package com.mongs.play.app.auth.dto.res;

import lombok.Builder;

@Builder
public record LogoutResDto(
        Long accountId
) {
}
