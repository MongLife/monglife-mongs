package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.exception.AlreadyConsumeInAppOrderException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class InAppOrder {

    private String orderId;

    private String productId;

    private String purchaseToken;

    private Boolean isPurchase;

    private Boolean isConsumed;

    private LocalDateTime purchasedAt;

    @Builder
    public InAppOrder(String orderId, String productId, String purchaseToken, Boolean isPurchase, Boolean isConsumed, LocalDateTime purchasedAt) {
        this.orderId = orderId;
        this.productId = productId;
        this.purchaseToken = purchaseToken;
        this.isPurchase = isPurchase;
        this.isConsumed = isConsumed;
        this.purchasedAt = purchasedAt;
    }

    /**
     * 인앱 주문 소비
     */
    public void consume() {

        if (!this.isPurchase || this.isConsumed) {
            throw new AlreadyConsumeInAppOrderException();
        }

        this.isPurchase = true;
        this.isConsumed = true;
    }
}
