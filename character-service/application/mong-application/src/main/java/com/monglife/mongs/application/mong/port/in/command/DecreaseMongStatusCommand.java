package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DecreaseMongStatusCommand {

    private final Long accountId;

    private final Long mongId;

    @Builder
    public DecreaseMongStatusCommand(Long accountId, Long mongId) {
        this.accountId = accountId;
        this.mongId = mongId;
    }
}
