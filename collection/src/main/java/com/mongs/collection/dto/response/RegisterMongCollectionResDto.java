package com.mongs.collection.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterMongCollectionResDto(
        Long memberId,
        String code,
        LocalDateTime createdAt
) {
}
