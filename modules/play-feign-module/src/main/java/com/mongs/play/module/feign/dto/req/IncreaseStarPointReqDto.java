package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record IncreaseStarPointReqDto(
        Long accountId,
        Integer addStarPoint
) {
}
