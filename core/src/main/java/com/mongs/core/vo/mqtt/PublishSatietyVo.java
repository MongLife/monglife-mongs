package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishSatietyVo(
        Long mongId,
        Double satiety
) {
}
