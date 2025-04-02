package com.mongs.play.app.common.dto.req;

import lombok.Builder;

@Builder
public record RegisterCodeVersionReqDto(
        String buildVersion
) {
}
