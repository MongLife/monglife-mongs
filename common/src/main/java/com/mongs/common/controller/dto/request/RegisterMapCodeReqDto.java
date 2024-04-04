package com.mongs.common.controller.dto.request;

import lombok.Builder;

@Builder
public record RegisterMapCodeReqDto(
        String name,
        String code,
        String buildVersion
) {
}
