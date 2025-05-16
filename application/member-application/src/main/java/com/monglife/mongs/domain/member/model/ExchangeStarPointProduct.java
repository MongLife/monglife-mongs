package com.monglife.mongs.domain.member.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExchangeStarPointProduct {

    private final String productId;

    private final Integer starPoint;

    @Builder
    public ExchangeStarPointProduct(String productId, Integer starPoint) {
        this.productId = productId;
        this.starPoint = starPoint;
    }
}
