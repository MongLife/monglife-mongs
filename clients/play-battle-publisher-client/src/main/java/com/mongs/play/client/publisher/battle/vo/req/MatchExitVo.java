package com.mongs.play.client.publisher.battle.vo.req;

import lombok.Builder;

@Builder
public record MatchExitVo(
        String roomId,
        String playerId
) {
}
