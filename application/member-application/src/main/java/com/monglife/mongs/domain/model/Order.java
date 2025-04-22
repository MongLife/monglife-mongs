package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Order {

    private Long orderId;

    private Long accountId;

    private String productId;

    private Double price;

    private String socialOrderId;

    private String purchaseToken;

    @Builder
    public Order(Long orderId, Long accountId, String productId, Double price, String socialOrderId, String purchaseToken) {
        this.orderId = orderId;
        this.accountId = accountId;
        this.productId = productId;
        this.price = price;
        this.socialOrderId = socialOrderId;
        this.purchaseToken = purchaseToken;
    }
}
