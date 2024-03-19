package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishTrainingVo(
        Long mongId,
        Integer payPoint,
        Double strength,
        Integer exp
) {
}
