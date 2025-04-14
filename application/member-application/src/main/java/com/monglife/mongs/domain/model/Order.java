package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.enums.OrderTypeCode;
import com.monglife.mongs.domain.exception.AlreadyConsumedOrderException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Order {

    private Long orderId;

    private Long accountId;

    private String productId;

    private OrderTypeCode orderTypeCode;

    private Double price;

    private String socialOrderId;

    private String purchaseToken;

    @Builder
    public Order(Long orderId, Long accountId, String productId, OrderTypeCode orderTypeCode, Double price, String socialOrderId, String purchaseToken) {
        this.orderId = orderId;
        this.accountId = accountId;
        this.productId = productId;
        this.orderTypeCode = orderTypeCode;
        this.price = price;
        this.socialOrderId = socialOrderId;
        this.purchaseToken = purchaseToken;
    }

    /**
     * 주문 소비
     */
    public void consume() {

        if (OrderTypeCode.CONSUMED.equals(this.orderTypeCode)) {
            throw new AlreadyConsumedOrderException();
        }

        this.orderTypeCode = OrderTypeCode.CONSUMED;
    }
}
