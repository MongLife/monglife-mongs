package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishStatusVo(
        Long mongId,
        Double health,
        Double satiety,
        Double strength,
        Double sleep,
        Boolean isSleeping,
        Integer poopCount
) {
}
