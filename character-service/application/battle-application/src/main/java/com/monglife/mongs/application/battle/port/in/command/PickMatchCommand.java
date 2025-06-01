package com.monglife.mongs.application.battle.port.in.command;

import com.monglife.mongs.domain.battle.enums.MatchPickCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PickMatchCommand {

    private final Long matchId;

    private final String playerId;

    private final String targetPlayerId;

    private final MatchPickCode pickCode;

    @Builder
    public PickMatchCommand(Long matchId, String playerId, String targetPlayerId, MatchPickCode pickCode) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.pickCode = pickCode;
    }
}
