package com.mongs.core.mqtt;

import lombok.Builder;

@Builder
public record PublishStatus(
        Long mongId,
        Double health,
        Double satiety,
        Double strength,
        Double sleep,
        Integer poopCount
) {
}
