package com.mongs.play.app.management.internal.dto.res;

import lombok.Builder;

@Builder
public record IncreasePoopCountResDto(
        Long accountId,
        Long mongId,
        Integer poopCount
) {
}
