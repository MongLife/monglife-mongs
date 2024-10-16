package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record PoopCleanMongResDto(
        Long mongId,
        Integer poopCount,
        Double exp
) {
}
