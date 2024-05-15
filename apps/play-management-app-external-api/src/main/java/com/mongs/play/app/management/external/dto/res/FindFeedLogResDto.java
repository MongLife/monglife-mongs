package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record FindFeedLogResDto(
        Long accountId,
        Long mongId,
        String code,
        Boolean isCanBuy
) {
}
