package com.mongs.common.controller.dto.request;

import lombok.Builder;

@Builder
public record RegisterMongCodeReqDto(
        String name,
        String code,
        String buildVersion
) {
}
