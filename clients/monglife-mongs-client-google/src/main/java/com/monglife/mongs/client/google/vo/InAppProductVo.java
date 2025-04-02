package com.monglife.mongs.client.google.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InAppProductVo {

    private final String productId;

    private final String productName;

    private final Double price;

    @Builder
    public InAppProductVo(String productId, String productName, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }
}
