package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record DecreaseSleepVo(
        Long mongId,
        Double sleep
) {
}
