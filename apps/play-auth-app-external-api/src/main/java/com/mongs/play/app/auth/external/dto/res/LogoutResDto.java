package com.mongs.play.app.auth.external.dto.res;

import lombok.Builder;

@Builder
public record LogoutResDto(
        Long accountId
) {
}