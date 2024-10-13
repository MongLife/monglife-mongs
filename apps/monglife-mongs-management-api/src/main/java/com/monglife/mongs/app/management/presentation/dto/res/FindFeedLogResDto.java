package com.monglife.mongs.app.management.presentation.dto.res;

import lombok.Builder;

@Builder
public record FindFeedLogResDto(
        Long mongId,
        String code,
        Boolean isCanBuy
) {
}
