package com.mongs.play.app.common.dto.res;

import lombok.Builder;

@Builder
public record RegisterMongCodeResDto(
        String code,
        String name,
        Integer level,
        Integer evolutionPoint
) {
}
