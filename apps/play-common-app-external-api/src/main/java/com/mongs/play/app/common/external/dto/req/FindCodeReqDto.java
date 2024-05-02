package com.mongs.play.app.common.external.dto.req;

import lombok.Builder;

@Builder
public record FindCodeReqDto(
        String buildVersion
) {
}
