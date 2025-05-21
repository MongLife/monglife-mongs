package com.monglife.mongs.application.battle.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchingQueuePlayersCommand {

    private final Integer matchPlayerCount;

    @Builder
    public MatchingQueuePlayersCommand(Integer matchPlayerCount) {
        this.matchPlayerCount = matchPlayerCount;
    }
}
