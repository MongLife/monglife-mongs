package com.monglife.mongs.application.device.port.in.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateTotalWalkingCountCommand {

    private final String deviceId;

    private final Integer totalWalkingCount;

    private final LocalDateTime deviceBootedDt;

    @Builder
    public UpdateTotalWalkingCountCommand(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt) {
        this.deviceId = deviceId;
        this.totalWalkingCount = totalWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
    }
}
