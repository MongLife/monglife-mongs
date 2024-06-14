package com.mongs.play.client.publisher.battle.vo.req;

import lombok.Builder;

@Builder
public record MatchPickVo(
        String roomId,
        String playerId,
        String pick
) {
}
