package com.mongs.play.app.player.internal.collection.dto.res;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterMongCollectionResDto(
        Long accountId,
        String code,
        LocalDateTime createdAt
) {
}