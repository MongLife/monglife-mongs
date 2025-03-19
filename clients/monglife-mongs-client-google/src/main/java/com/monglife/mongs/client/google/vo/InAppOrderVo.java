package com.monglife.mongs.client.google.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class InAppOrderVo {

    private final String orderId;

    private final String productId;

    private final String purchaseToken;

    private final Boolean isPurchase;

    private final Boolean isConsume;

    private final LocalDateTime purchasedAt;

    @Builder
    public InAppOrderVo(String orderId, String productId, String purchaseToken, Boolean isPurchase, Boolean isConsume, LocalDateTime purchasedAt) {
        this.orderId = orderId;
        this.productId = productId;
        this.purchaseToken = purchaseToken;
        this.isPurchase = isPurchase;
        this.isConsume = isConsume;
        this.purchasedAt = purchasedAt;
    }
}
