package com.mongs.play.app.common.internal.dto.req;

import lombok.Builder;

@Builder
public record RegisterMapCodeReqDto(
        String name,
        String code,
        String buildVersion
) {
}
