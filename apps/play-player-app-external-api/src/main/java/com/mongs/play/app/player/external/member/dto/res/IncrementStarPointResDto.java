package com.mongs.play.app.player.external.member.dto.res;

import lombok.Builder;

@Builder
public record IncrementStarPointResDto(
        Long accountId,
        Integer starPoint
) {
}
