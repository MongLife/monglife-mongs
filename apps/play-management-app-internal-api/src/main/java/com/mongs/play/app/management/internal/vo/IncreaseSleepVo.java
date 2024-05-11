package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record IncreaseSleepVo(
        Long mongId,
        Double sleep
) {
}
