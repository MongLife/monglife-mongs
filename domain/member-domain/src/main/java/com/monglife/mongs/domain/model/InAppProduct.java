package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InAppProduct {

    private final String productId;

    private final String productName;

    private final Double price;

    @Builder
    public InAppProduct(String productId, String productName, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }
}
