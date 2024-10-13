package com.monglife.mongs.app.management.business.vo;

import lombok.Builder;

@Builder
public record EvolutionReadyVo(
        Long mongId,
        String shiftCode
) {
}
