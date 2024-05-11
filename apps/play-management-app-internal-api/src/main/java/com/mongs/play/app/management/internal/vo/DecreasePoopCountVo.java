package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record DecreasePoopCountVo(
        Long mongId,
        Integer poopCount
) {
}
