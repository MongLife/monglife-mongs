package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record DecreaseWeightVo(
        Long mongId,
        Double weight
) {
}
