package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishSleepingVo(
        Long mongId,
        Boolean isSleeping
) {
}
