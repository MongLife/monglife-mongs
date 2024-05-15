package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record FeedMongResDto(
        Long accountId,
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
