package com.mongs.play.domain.mong.vo;

import lombok.Builder;

@Builder
public record MongStatusPercentVo(
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp
) {
}
