package com.mongs.play.module.feign.dto.res;

import lombok.Builder;

@Builder
public record RegisterMapCollectionResDto(
        Long accountId,
        String mapCode
) {
}
