package com.mongs.play.client.publisher.battle.vo.res;

import lombok.Builder;

@Builder
public record MatchPlayerVo(
        String playerId,
        String mongCode,
        Double hp,
        String state
) {
}
