package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record SleepingMongVo(
        Long mongId,
        Boolean isSleeping
) {
}
