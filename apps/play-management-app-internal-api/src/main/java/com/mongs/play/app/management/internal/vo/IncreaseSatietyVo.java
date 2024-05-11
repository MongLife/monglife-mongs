package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record IncreaseSatietyVo(
        Long mongId,
        Double satiety
) {
}
