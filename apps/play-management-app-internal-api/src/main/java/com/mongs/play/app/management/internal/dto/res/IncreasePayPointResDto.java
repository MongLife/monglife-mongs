package com.mongs.play.app.management.internal.dto.res;

import lombok.Builder;

@Builder
public record IncreasePayPointResDto(
        Long accountId,
        Long mongId,
        Integer payPoint
) {
}
