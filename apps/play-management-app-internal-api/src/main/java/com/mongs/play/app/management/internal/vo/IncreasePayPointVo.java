package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record IncreasePayPointVo(
        Long accountId,
        Long mongId,
        Integer payPoint
) {
}
