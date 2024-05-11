package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record IncreaseStrengthVo(
        Long mongId,
        Double strength
) {
}
