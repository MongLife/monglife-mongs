package com.mongs.play.module.feign.dto.res;

import lombok.Builder;

@Builder
public record IncreasePoopCountResDto(
        Long mongId,
        Integer poopCount
) {
}