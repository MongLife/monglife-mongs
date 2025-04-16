package com.monglife.mongs.adapter.transaction.commit;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeCurrentWalkingCountEventDto {

    private Long mongId;

    private Integer walkingCount;

    private Integer payPoint;

    @Builder
    public ExchangeCurrentWalkingCountEventDto(Long mongId, Integer walkingCount, Integer payPoint) {
        this.mongId = mongId;
        this.walkingCount = walkingCount;
        this.payPoint = payPoint;
    }
}
