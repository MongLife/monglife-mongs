package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record IncreaseStatusReqDto(
        Long mongId,
        Double addWeight,
        Double addStrength,
        Double addSatiety,
        Double addHealthy,
        Double addSleep
) {
}
