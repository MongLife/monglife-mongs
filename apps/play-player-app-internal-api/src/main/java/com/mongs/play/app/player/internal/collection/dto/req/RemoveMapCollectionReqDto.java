package com.mongs.play.app.player.internal.collection.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RemoveMapCollectionReqDto(
        Long accountId,
        @NotEmpty
        @NotBlank
        String mapCode
) {
}
