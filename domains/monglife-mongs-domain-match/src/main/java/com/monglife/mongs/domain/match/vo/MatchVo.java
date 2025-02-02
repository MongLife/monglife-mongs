package com.monglife.mongs.domain.match.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
public class MatchVo {

    private final Integer round;

    private final Set<MatchPlayerVo> matchPlayers;

    private final Boolean isLastRound;

    @Builder
    public MatchVo(Integer round, Set<MatchPlayerVo> matchPlayers, Boolean isLastRound) {
        this.round = round;
        this.matchPlayers = matchPlayers;
        this.isLastRound = isLastRound;
    }
}
