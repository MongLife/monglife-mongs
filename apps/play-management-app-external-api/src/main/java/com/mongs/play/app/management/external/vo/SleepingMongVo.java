package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record SleepingMongVo(
        Long accountId,
        Long mongId,
        Boolean isSleeping
) {
}
