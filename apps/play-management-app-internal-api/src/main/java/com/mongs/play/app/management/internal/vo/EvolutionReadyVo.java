package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record EvolutionReadyVo(
        Long mongId,
        String shiftCode
) {
}
