package com.mongs.play.client.publisher.mong.vo;

import lombok.Builder;

@Builder
public record MongPayPointVo(
        Long mongId,
        Integer payPoint
) {
}
