package com.monglife.mongs.app.management.presentation.dto.res;

import lombok.Builder;

@Builder
public record FeedMongResDto(
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp,
        Integer payPoint
) {
}