package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishStateVo(
        Long mongId,
        String stateCode
) {
}
