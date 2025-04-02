package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record EvolutionScheduleReqDto(
        Long mongId
) {
}
