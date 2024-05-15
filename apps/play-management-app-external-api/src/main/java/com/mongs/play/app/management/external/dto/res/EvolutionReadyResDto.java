package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record EvolutionReadyResDto(
        Long accountId,
        Long mongId,
        String shiftCode
) {
}
