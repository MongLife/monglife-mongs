package com.mongs.play.client.publisher.battle.vo.res;

import lombok.Builder;

import java.util.List;

@Builder
public record MatchOverVo(
        String roomId,
        Integer round,
        MatchPlayerVo winPlayer,
        List<MatchPlayerVo> matchPlayerVoList
) {
}
