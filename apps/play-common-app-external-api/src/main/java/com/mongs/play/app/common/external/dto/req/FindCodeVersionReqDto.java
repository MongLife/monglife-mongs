package com.mongs.play.app.common.external.dto.req;

import lombok.Builder;

@Builder
public record FindCodeVersionReqDto(
        String buildVersion,
        String codeIntegrity
) {
}
