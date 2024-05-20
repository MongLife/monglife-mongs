package com.mongs.play.client.publisher.mong.vo;

import lombok.Builder;

@Builder
public record MongIsSleepingVo(
        Long mongId,
        Boolean isSleeping
) {
}
