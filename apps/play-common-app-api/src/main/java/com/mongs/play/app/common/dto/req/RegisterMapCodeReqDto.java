package com.mongs.play.app.common.dto.req;

import lombok.Builder;

@Builder
public record RegisterMapCodeReqDto(
        String name,
        String code,
        String buildVersion
) {
}
