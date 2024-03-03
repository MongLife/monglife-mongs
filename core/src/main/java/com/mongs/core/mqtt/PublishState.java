package com.mongs.core.mqtt;

import lombok.Builder;

@Builder
public record PublishState(
        Long mongId,
        String stateCode
) {
}
