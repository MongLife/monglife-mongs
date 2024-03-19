package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishStrengthVo(
        Long mongId,
        Double strength
) {
}
