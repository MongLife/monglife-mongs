package com.mongs.auth.dto.response;

import lombok.Builder;

@Builder
public record LogoutResDto(
        Long accountId
) {
}
