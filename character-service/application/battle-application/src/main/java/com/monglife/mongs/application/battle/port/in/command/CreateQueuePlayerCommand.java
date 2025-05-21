package com.monglife.mongs.application.battle.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateQueuePlayerCommand {

    private final Long mongId;

    private final String deviceId;

    private final Long accountId;

    @Builder
    public CreateQueuePlayerCommand(Long mongId, String deviceId, Long accountId) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
    }
}
