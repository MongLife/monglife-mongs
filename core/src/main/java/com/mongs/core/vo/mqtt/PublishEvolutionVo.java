package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishEvolutionVo(
        Long mongId,
        String mongCode,
        String shiftCode,
        String stateCode,
        Integer exp
) {
}
