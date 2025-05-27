package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EvolutionReadyMongCommand {

    private final Long accountId;

    private final Long mongId;

    @Builder
    public EvolutionReadyMongCommand(Long accountId, Long mongId) {
        this.accountId = accountId;
        this.mongId = mongId;
    }
}
