package com.mongs.play.app.common.internal.dto.req;

import lombok.Builder;

@Builder
public record RegisterCodeVersionReqDto(
        String buildVersion
) {
}
