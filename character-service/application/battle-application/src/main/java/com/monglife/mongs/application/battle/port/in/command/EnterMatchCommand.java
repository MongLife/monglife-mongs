package com.monglife.mongs.application.battle.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EnterMatchCommand {

    private final Long matchId;

    private final String playerId;

    @Builder
    public EnterMatchCommand(Long matchId, String playerId) {
        this.matchId = matchId;
        this.playerId = playerId;
    }
}
