package com.mongs.play.app.common.dto.res;

import lombok.Builder;

@Builder
public record RegisterCodeVersionResDto(
        String buildVersion
) {
}
