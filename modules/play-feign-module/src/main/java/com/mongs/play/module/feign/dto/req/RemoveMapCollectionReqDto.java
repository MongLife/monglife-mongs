package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record RemoveMapCollectionReqDto(
        Long accountId,
        String mapCode
) {
}
