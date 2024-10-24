package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record SleepingMongResDto(
        Long mongId,
        Boolean isSleeping
) {
}
