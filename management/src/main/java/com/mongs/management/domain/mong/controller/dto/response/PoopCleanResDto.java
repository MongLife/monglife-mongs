package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

@Builder
public record PoopCleanResDto(
        Long mongId,
        Integer poopCount,
        Double exp
) {
}
