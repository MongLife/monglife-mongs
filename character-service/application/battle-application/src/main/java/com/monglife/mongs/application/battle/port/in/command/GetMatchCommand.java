package com.monglife.mongs.application.battle.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMatchCommand {

    private final Long matchId;

    @Builder
    public GetMatchCommand(Long matchId) {
        this.matchId = matchId;
    }
}
