package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishDeleteVo(
        Long mongId
) {
}
