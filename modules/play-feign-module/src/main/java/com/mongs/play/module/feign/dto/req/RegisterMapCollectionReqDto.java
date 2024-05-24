package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record RegisterMapCollectionReqDto(
        Long accountId,
        String mapCode
) {
}
