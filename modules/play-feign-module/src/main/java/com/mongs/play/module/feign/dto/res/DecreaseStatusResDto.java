package com.mongs.play.module.feign.dto.res;

import lombok.Builder;

@Builder
public record DecreaseStatusResDto(
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep
) {
}
