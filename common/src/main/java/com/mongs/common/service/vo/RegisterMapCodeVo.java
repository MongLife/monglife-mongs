package com.mongs.common.service.vo;

import lombok.Builder;

@Builder
public record RegisterMapCodeVo(
        String name,
        String code
) {
}
