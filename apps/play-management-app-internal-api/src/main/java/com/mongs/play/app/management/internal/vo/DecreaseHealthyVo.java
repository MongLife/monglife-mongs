package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record DecreaseHealthyVo(
        Long mongId,
        Double healthy
) {
}
