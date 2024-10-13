package com.monglife.mongs.app.management.business.vo;

import lombok.Builder;

@Builder
public record SleepingMongVo(
        Long mongId,
        Boolean isSleeping
) {
}
