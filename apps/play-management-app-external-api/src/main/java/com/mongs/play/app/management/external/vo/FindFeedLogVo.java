package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record FindFeedLogVo(
        Long mongId,
        String code,
        Boolean isCanBuy
) {
}
