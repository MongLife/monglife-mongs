package com.monglife.mongs.app.management.presentation.dto.res;

import lombok.Builder;

@Builder
public record PoopCleanMongResDto(
        Long mongId,
        Integer poopCount,
        Double exp
) {
}
