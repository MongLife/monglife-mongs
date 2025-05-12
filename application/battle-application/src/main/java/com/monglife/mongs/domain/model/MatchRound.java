package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.enums.MatchRoundCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MatchRound {

    private final Long matchId;

    private final String playerId;

    private final String targetPlayerId;

    private final Integer round;

    private final MatchRoundCode matchRoundCode;

    private final Double value;

    @Builder
    public MatchRound(Long matchId, String playerId, String targetPlayerId, Integer round, MatchRoundCode matchRoundCode, Double value) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.round = round;
        this.matchRoundCode = matchRoundCode;
        this.value = value;
    }
}
