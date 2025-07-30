package com.monglife.mongs.domain.member.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Order {

    private final Long orderId;

    private final Long accountId;

    private final String productId;

    private final Double price;

    private final String socialOrderId;

    private final String purchaseToken;

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
