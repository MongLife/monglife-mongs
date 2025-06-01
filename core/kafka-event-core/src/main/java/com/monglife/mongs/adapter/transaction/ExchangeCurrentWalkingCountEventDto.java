package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ExchangeCurrentWalkingCountEventDto {

    private String deviceId;

    private Long mongId;

    private Integer walkingCount;

    private Integer payPoint;

    @Builder
    public ExchangeCurrentWalkingCountEventDto(String deviceId, Long mongId, Integer walkingCount, Integer payPoint) {
        this.deviceId = deviceId;
        this.mongId = mongId;
        this.walkingCount = walkingCount;
        this.payPoint = payPoint;
    }
}
