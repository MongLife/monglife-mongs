package com.mongs.play.app.player.internal.member.dto.res;

import lombok.Builder;

@Builder
public record IncreaseStarPointResDto(
        Long accountId,
        Integer starPoint
) {
}
