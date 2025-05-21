package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IncreaseMongStatusCommand {

    private final Long accountId;

    private final Long mongId;

    @Builder
    public IncreaseMongStatusCommand(Long accountId, Long mongId) {
        this.accountId = accountId;
        this.mongId = mongId;
    }
}
