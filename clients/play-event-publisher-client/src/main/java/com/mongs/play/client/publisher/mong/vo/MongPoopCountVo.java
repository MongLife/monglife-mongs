package com.mongs.play.client.publisher.mong.vo;

import lombok.Builder;

@Builder
public record MongPoopCountVo(
        Long mongId,
        Integer poopCount
) {
}
