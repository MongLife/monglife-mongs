package com.monglife.mongs.application.device.port.in.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExchangeCurrentWalkingCountCommand {

    private final Long accountId;

    private final String deviceId;

    private final Long mongId;

    private final Integer walkingCount;

    @Builder
    public ExchangeCurrentWalkingCountCommand(Long accountId, String deviceId, Long mongId, Integer walkingCount) {
        this.accountId = accountId;
        this.deviceId = deviceId;
        this.mongId = mongId;
        this.walkingCount = walkingCount;
    }
}
