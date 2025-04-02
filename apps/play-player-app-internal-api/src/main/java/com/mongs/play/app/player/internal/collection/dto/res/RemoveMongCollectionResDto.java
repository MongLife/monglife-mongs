package com.mongs.play.app.player.internal.collection.dto.res;

import lombok.Builder;

@Builder
public record RemoveMongCollectionResDto(
        Long accountId,
        String code
) {
}
