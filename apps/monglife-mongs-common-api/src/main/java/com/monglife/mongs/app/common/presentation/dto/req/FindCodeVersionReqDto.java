package com.monglife.mongs.app.common.presentation.dto.req;

import lombok.Builder;

@Builder
public record FindCodeVersionReqDto(
        String buildVersion,
        String codeIntegrity
) {
}
