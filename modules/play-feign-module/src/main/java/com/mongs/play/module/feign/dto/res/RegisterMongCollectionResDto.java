package com.mongs.play.module.feign.dto.res;

import lombok.Builder;

@Builder
public record RegisterMongCollectionResDto(
        Long accountId,
        Integer starPoint
) {
}
