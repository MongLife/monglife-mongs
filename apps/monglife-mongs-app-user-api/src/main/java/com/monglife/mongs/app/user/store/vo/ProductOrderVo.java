package com.monglife.mongs.app.user.store.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductOrderVo {

    private final String productId;

    private final String orderId;

    private final Boolean isConsume;

    @Builder
    public ProductOrderVo(String productId, String orderId, Boolean isConsume) {
        this.productId = productId;
        this.orderId = orderId;
        this.isConsume = isConsume;
    }
}
