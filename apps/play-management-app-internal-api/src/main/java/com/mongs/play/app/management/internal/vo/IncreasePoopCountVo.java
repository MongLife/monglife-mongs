package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record IncreasePoopCountVo(
        Long mongId,
        Integer poopCount
) {
}
