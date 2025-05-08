package com.monglife.mongs.application.device.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IncreaseCurrentWalkingCountCommand {

    private final String deviceId;

    private final Integer walkingCount;

    @Builder
    public IncreaseCurrentWalkingCountCommand(String deviceId, Integer walkingCount) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
    }
}
