package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InAppProduct {

    private String productId;

    private String productName;

    private Double price;

    @Builder
    public InAppProduct(String productId, String productName, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }
}
