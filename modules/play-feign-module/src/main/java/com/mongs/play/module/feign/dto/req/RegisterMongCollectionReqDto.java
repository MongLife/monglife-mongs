package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record RegisterMongCollectionReqDto(
        Long accountId,
        String mongCode
) {
}
