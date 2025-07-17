package com.monglife.mongs.domain.member.model;

import com.monglife.mongs.domain.member.enums.OrderPurchaseTypeCode;
import com.monglife.mongs.domain.member.enums.OrderTypeCode;
import com.monglife.mongs.domain.member.exception.AlreadyConsumedInAppOrderException;
import com.monglife.mongs.domain.member.exception.PaymentNotCompletedInAppOrderException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class InAppOrder {

    private final String socialOrderId;

    private final String productId;

    private final String purchaseToken;

    private final OrderPurchaseTypeCode orderPurchaseTypeCode;

    private OrderTypeCode orderTypeCode;

    private final LocalDateTime purchasedAt;

    @Builder
    public InAppOrder(String socialOrderId, String productId, String purchaseToken, OrderPurchaseTypeCode orderPurchaseTypeCode, OrderTypeCode orderTypeCode, LocalDateTime purchasedAt) {
        this.socialOrderId = socialOrderId;
        this.productId = productId;
        this.purchaseToken = purchaseToken;
        this.orderPurchaseTypeCode = orderPurchaseTypeCode;
        this.orderTypeCode = orderTypeCode;
        this.purchasedAt = purchasedAt;
    }

    public Boolean isPayed() {
        return OrderPurchaseTypeCode.PAYED.equals(this.orderPurchaseTypeCode);
    }

    public Boolean isConsumed() {
        return OrderTypeCode.CONSUMED.equals(this.orderTypeCode);
    }

    /**
     * 인앱 주문 소비
     */
    public void consume() {

        if (!this.isPayed()) {
            throw new PaymentNotCompletedInAppOrderException();
        } else if (this.isConsumed()) {
            throw new AlreadyConsumedInAppOrderException();
        }

        this.orderTypeCode = OrderTypeCode.CONSUMED;
    }
}
