package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishFeedVo(
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double health,
        Double sleep,
        Double exp,
        Integer payPoint
) {
}
