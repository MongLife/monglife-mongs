package com.mongs.play.client.publisher.mong.vo;

import lombok.Builder;

@Builder
public record MongCodeVo(
        Long mongId,
        String mongCode
) {
}
