package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ExchangeCurrentWalkingCountEventDto {

    private Long accountId;

    private String deviceId;

    private Long mongId;

    private Integer walkingCount;

    private Integer payPoint;

    @Builder
    public ExchangeCurrentWalkingCountEventDto(Long accountId, String deviceId, Long mongId, Integer walkingCount, Integer payPoint) {
        this.accountId = accountId;
        this.deviceId = deviceId;
        this.mongId = mongId;
        this.walkingCount = walkingCount;
        this.payPoint = payPoint;
    }
}
