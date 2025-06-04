package com.monglife.mongs.adapter.transaction;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ExchangeStarPointEventDto {

    private Long accountId;

    private Long mongId;

    private Integer starPoint;

    private Integer payPoint;

    @Builder
    public ExchangeStarPointEventDto(Long accountId, Long mongId, Integer starPoint, Integer payPoint) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.starPoint = starPoint;
        this.payPoint = payPoint;
    }
}
