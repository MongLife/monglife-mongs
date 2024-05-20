package com.mongs.play.client.publisher.mong.vo;

import lombok.Builder;

@Builder
public record MongShiftVo(
        Long mongId,
        String shiftCode
) {
}
