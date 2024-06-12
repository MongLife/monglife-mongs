package com.mongs.play.app.common.vo;

import lombok.Builder;

@Builder
public record RegisterMongCodeVo(
        String code,
        String name,
        Integer level,
        Integer evolutionPoint,
        String buildVersion
) {
}
