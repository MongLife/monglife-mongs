package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishEvolutionVo(
        Long mongId,
        String mongCode,
        Double weight,
        Double strength,
        Double satiety,
        Double health,
        Double sleep,
        String shiftCode,
        String stateCode,
        Double exp
) {
}
