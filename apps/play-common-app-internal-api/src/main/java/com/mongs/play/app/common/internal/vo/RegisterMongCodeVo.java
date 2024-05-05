package com.mongs.play.app.common.internal.vo;

import lombok.Builder;

@Builder
public record RegisterMongCodeVo(
        String code,
        String name,
        String buildVersion
) {
}
