package com.mongs.play.client.publisher.battle.vo.res;

import lombok.Builder;

@Builder
public record MatchFindVo(
        String roomId,
        String playerId
) {
}
