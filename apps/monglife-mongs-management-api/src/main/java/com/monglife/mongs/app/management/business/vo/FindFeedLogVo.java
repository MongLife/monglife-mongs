package com.monglife.mongs.app.management.business.vo;

import lombok.Builder;

@Builder
public record FindFeedLogVo(
        Long mongId,
        String code,
        Boolean isCanBuy
) {
}
