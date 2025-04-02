package com.mongs.play.app.gateway.dto.req;

import lombok.Builder;

@Builder
public record PassportReqDto(
        String accessToken
) {
}
