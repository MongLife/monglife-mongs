package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record EvolutionReadyVo(
        Long mongId,
        String shiftCode
) {
}