package com.monglife.mongs.application.device.port.out.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class StepPublishDto {

    private final String deviceId;

    private final Integer walkingCount;

    private final Integer totalWalkingCount;

    private final Integer consumeWalkingCount;

    private final LocalDateTime deviceBootedDt;

    @Builder
    public StepPublishDto(String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
    }
}
