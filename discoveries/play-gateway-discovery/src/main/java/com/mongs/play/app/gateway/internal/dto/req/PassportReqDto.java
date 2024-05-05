package com.mongs.play.app.gateway.internal.dto.req;

import lombok.Builder;

@Builder
public record PassportReqDto(
        String accessToken
) {
}
