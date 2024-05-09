package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record TrainingMongResDto(
        Long mongId,
        Double strength,
        Double exp,
        Integer payPoint
) {
}
