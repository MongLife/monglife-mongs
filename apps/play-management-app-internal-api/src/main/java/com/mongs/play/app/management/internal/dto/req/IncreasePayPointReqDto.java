package com.mongs.play.app.management.internal.dto.req;

import lombok.Builder;

@Builder
public record IncreasePayPointReqDto(
        Long mongId,
        Integer addPayPoint
) {
}
