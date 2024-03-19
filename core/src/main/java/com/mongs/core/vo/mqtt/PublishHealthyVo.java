package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishHealthyVo(
        Long mongId,
        Double health
) {
}
