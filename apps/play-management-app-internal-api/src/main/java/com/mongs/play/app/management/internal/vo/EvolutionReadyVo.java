package com.mongs.play.app.management.internal.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import lombok.Builder;

@Builder
public record EvolutionReadyVo(
        Long accountId,
        Long mongId,
        MongShift shift
) {
}