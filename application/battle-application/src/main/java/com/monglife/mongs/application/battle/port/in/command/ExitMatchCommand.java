package com.monglife.mongs.application.battle.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExitMatchCommand {

    private final Long matchId;

    private final String playerId;

    @Builder
    public ExitMatchCommand(Long matchId, String playerId) {
        this.matchId = matchId;
        this.playerId = playerId;
    }
}
