package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishSleepVo(
        Long mongId,
        Double sleep
) {
}
