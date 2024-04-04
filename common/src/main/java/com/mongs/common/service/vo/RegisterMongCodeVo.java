package com.mongs.common.service.vo;

import lombok.Builder;

@Builder
public record RegisterMongCodeVo(
        String name,
        String code
) {
}
