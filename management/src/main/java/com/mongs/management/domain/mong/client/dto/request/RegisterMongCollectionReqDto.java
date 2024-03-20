package com.mongs.management.domain.mong.client.dto.request;

import lombok.Builder;

@Builder
public record RegisterMongCollectionReqDto(
        String mongCode
) {
}
