package com.monglife.mongs.application.device.port.out.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExchangeCurrentWalkingCountDto {

    private final String deviceId;

    private final Long mongId;

    private final Integer payPoint;

    private final Integer walkingCount;

    @Builder
    public ExchangeCurrentWalkingCountDto(String deviceId, Long mongId, Integer payPoint, Integer walkingCount) {
        this.deviceId = deviceId;
        this.mongId = mongId;
        this.payPoint = payPoint;
        this.walkingCount = walkingCount;
    }
}
