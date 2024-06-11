package com.mongs.play.app.common.dto.res;

import lombok.Builder;

@Builder
public record RegisterMapCodeResDto(
        String name,
        String code
) {
}
