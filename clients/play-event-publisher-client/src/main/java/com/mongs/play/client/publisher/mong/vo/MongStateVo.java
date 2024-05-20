package com.mongs.play.client.publisher.mong.vo;

import lombok.Builder;

@Builder
public record MongStateVo(
        Long mongId,
        String stateCode
) {
}
