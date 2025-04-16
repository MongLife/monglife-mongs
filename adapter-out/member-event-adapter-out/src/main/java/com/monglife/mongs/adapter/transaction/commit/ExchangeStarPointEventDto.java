package com.monglife.mongs.adapter.transaction.commit;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeStarPointEventDto {

    private Long mongId;

    private Integer starPoint;

    private Integer payPoint;

    @Builder
    public ExchangeStarPointEventDto(Long mongId, Integer starPoint, Integer payPoint) {
        this.mongId = mongId;
        this.starPoint = starPoint;
        this.payPoint = payPoint;
    }
}
