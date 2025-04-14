package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.enums.OrderPurchaseTypeCode;
import com.monglife.mongs.domain.enums.OrderTypeCode;
import com.monglife.mongs.domain.exception.AlreadyConsumedInAppOrderException;
import com.monglife.mongs.domain.exception.PaymentNotCompletedInAppOrderException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class InAppOrder {

    private String socialOrderId;

    private String productId;

    private String purchaseToken;

    private OrderPurchaseTypeCode orderPurchaseTypeCode;

    private OrderTypeCode orderTypeCode;

    private LocalDateTime purchasedAt;

    @Builder
    public InAppOrder(String socialOrderId, String productId, String purchaseToken, OrderPurchaseTypeCode orderPurchaseTypeCode, OrderTypeCode orderTypeCode, LocalDateTime purchasedAt) {
        this.socialOrderId = socialOrderId;
        this.productId = productId;
        this.purchaseToken = purchaseToken;
        this.orderPurchaseTypeCode = orderPurchaseTypeCode;
        this.orderTypeCode = orderTypeCode;
        this.purchasedAt = purchasedAt;
    }

    /**
     * 인앱 주문 소비
     */
    public void consume() {

        if (!OrderPurchaseTypeCode.PAYED.equals(this.orderPurchaseTypeCode)) {
            throw new PaymentNotCompletedInAppOrderException();
        } else if (OrderTypeCode.CONSUMED.equals(this.orderTypeCode)) {
            throw new AlreadyConsumedInAppOrderException();
        }

        this.orderTypeCode = OrderTypeCode.CONSUMED;
    }
}
