package com.mongs.play.client.publisher.battle.vo.res;

import lombok.Builder;

@Builder
public record MatchOverVo(
        String roomId,
        MatchPlayerVo winPlayer,
        MatchPlayerVo losePlayer
) {
}
