package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record DecreaseSatietyVo(
        Long mongId,
        Double satiety
) {
}
