package com.mongs.play.app.common.internal.dto.res;

import lombok.Builder;

@Builder
public record RegisterMongCodeResDto(
        String code,
        String name,
        Integer level,
        Integer evolutionPoint
) {
}
