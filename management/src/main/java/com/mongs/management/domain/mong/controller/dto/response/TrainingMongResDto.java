package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

@Builder
public record TrainingMongResDto(
        Long mongId,
        Double strength,
        Double exp,
        Integer payPoint
) {
}
