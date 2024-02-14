package com.mongs.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResDto(String accessToken, String refreshToken) {
}
