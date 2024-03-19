package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishStrokeVo(
        Long mongId,
        Integer exp
) {
}
