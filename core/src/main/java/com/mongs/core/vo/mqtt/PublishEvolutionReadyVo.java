package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishEvolutionReadyVo(
        Long mongId,
        String shiftCode
) {
}
