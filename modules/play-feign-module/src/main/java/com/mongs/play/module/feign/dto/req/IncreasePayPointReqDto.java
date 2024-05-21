package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record IncreasePayPointReqDto(
        Long mongId,
        Integer addPayPoint
) {
}
