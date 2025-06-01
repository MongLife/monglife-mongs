package com.monglife.mongs.application.device.port.out.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateStepVo {

    private final String deviceId;

    private final Integer walkingCount;

    private final Integer totalWalkingCount;

    private final Integer consumeWalkingCount;

    private final LocalDateTime deviceBootedAt;

    @Builder
    public CreateStepVo(String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedAt) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedAt = deviceBootedAt;
    }
}
