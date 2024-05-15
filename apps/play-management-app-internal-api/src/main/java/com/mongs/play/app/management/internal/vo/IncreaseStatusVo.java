package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record IncreaseStatusVo(
        Long accountId,
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep
) {
}