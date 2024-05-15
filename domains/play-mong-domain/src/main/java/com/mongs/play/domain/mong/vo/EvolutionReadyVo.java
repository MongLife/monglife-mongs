package com.mongs.play.domain.mong.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import lombok.Builder;

@Builder
public record EvolutionReadyVo(
        Long accountId,
        Long mongId,
        String shiftCode
) {
}
