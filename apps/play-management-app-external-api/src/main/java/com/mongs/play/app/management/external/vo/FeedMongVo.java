package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record FeedMongVo(
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
