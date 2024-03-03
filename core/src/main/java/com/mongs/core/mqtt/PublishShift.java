package com.mongs.core.mqtt;

import lombok.Builder;

@Builder
public record PublishShift(
        Long mongId,
        String shiftCode
) {
}
