package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record IncreaseWeightVo(
        Long mongId,
        Double weight
) {
}
