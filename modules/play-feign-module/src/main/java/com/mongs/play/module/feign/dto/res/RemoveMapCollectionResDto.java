package com.mongs.play.module.feign.dto.res;

import lombok.Builder;

@Builder
public record RemoveMapCollectionResDto(
        Long accountId,
        String code
) {
}
