package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishTrainingVo(
        Long mongId,
        Double strength,
        Double exp,
        Integer payPoint
) {
}
