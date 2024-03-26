package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishWeightVo(
        Long mongId,
        Double weight
) {
}
