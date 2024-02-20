package com.mongs.gateway.dto.request;

import lombok.Builder;

@Builder
public record PassportReqDto(
        String accessToken
) {
}
