package com.mongs.core.mqtt;

import lombok.Builder;

@Builder
public record PublishStatus(
        Long mongId,
        Float health,
        Float satiety,
        Float strength,
        Float sleep
) {
}
