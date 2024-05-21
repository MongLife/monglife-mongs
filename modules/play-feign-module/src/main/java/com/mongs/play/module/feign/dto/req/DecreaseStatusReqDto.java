package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record DecreaseStatusReqDto(
        Long mongId,
        Double subWeight,
        Double subStrength,
        Double subSatiety,
        Double subHealthy,
        Double subSleep
) {
}
