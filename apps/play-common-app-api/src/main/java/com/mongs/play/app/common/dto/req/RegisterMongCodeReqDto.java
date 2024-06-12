package com.mongs.play.app.common.dto.req;

import lombok.Builder;

@Builder
public record RegisterMongCodeReqDto(
        String name,
        String code,
        Integer level,
        Integer evolutionPoint,
        String buildVersion
) {
}
