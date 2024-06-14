package com.mongs.play.client.publisher.battle.vo.res;

import lombok.Builder;

import java.util.List;

@Builder
public record MatchVo(
        String roomId,
        Integer round,
        List<MatchPlayerVo> matchPlayerVoList
) {
}
