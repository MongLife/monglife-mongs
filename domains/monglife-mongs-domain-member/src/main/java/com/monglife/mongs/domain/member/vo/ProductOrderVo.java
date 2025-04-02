package com.monglife.mongs.domain.member.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductOrderVo {

    private final Long accountId;

    private final String productId;

    private final String orderId;

    private final String purchaseToken;

    @Builder
    public ProductOrderVo(Long accountId, String productId, String orderId, String purchaseToken) {
        this.accountId = accountId;
        this.productId = productId;
        this.orderId = orderId;
        this.purchaseToken = purchaseToken;
    }
}
