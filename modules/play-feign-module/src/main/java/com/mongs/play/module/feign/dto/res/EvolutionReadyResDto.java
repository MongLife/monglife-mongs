package com.mongs.play.module.feign.dto.res;

import lombok.Builder;

@Builder
public record EvolutionReadyResDto(
        Long mongId,
        String shiftCode
) {
}
