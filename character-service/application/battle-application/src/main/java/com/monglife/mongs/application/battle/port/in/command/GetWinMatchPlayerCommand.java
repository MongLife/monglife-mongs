package com.monglife.mongs.application.battle.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetWinMatchPlayerCommand {

    private final Long matchId;

    @Builder
    public GetWinMatchPlayerCommand(Long matchId) {
        this.matchId = matchId;
    }
}
