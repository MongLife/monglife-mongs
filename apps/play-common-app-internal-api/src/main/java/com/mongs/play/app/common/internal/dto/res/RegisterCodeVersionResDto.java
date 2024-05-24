package com.mongs.play.app.common.internal.dto.res;

import lombok.Builder;

@Builder
public record RegisterCodeVersionResDto(
        String buildVersion
) {
}
