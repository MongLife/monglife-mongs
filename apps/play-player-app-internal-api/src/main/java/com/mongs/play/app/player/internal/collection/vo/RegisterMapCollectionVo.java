package com.mongs.play.app.player.internal.collection.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterMapCollectionVo(
        Long accountId,
        String code,
        LocalDateTime createdAt
) {
}
