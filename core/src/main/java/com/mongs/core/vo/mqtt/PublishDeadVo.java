package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishDeadVo(
        Long mongId
) {
}
