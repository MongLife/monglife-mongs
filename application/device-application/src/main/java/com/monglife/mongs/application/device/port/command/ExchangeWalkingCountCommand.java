package com.monglife.mongs.application.device.port.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExchangeWalkingCountCommand {

    private final Long accountId;

    private final Integer walkingCount;

    @Builder
    public ExchangeWalkingCountCommand(Long accountId, Integer walkingCount) {
        this.accountId = accountId;
        this.walkingCount = walkingCount;
    }
}
