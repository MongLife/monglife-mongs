package com.mongs.play.client.publisher.battle.vo.req;

import lombok.Builder;

@Builder
public record MatchEnterVo(
        String roomId,
        String playerId
) {
}
