package com.mongs.play.app.common.internal.vo;

import lombok.Builder;

@Builder
public record RegisterMapCodeVo(
        String name,
        String code
) {
}
