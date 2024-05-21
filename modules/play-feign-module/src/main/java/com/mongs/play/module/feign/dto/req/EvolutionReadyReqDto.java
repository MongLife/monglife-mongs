package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record EvolutionReadyReqDto(
        Long mongId
) {
}
