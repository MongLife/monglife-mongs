package com.mongs.play.client.publisher.mong.vo;

import lombok.Builder;

@Builder
public record MongStatusVo(
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp
) {
}
