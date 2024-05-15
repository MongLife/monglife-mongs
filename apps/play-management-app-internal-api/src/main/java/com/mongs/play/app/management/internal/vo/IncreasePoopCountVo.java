package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record IncreasePoopCountVo(
        Long accountId,
        Long mongId,
        Integer poopCount
) {
}
