package com.mongs.management.domain.management.client.dto.request;

import lombok.Builder;

@Builder
public record RegisterMapCollectionReqDto(
        String mapCode
) {
}
