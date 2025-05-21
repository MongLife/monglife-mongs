package com.monglife.mongs.application.member.port.out.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateOrderVo {

    private final Long accountId;

    private final String productId;

    private final Double price;

    private final String socialOrderId;

    private final String purchaseToken;

    @Builder
    public CreateOrderVo(Long accountId, String productId, Double price, String socialOrderId, String purchaseToken) {
        this.accountId = accountId;
        this.productId = productId;
        this.price = price;
        this.socialOrderId = socialOrderId;
        this.purchaseToken = purchaseToken;
    }
}
