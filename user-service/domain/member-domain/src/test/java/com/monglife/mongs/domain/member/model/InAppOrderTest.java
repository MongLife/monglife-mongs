package com.monglife.mongs.domain.member.model;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.domain.member.enums.OrderPurchaseTypeCode;
import com.monglife.mongs.domain.member.enums.OrderTypeCode;
import com.monglife.mongs.domain.member.exception.AlreadyConsumedInAppOrderException;
import com.monglife.mongs.domain.member.exception.PaymentNotCompletedInAppOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InAppOrderTest {

    private static final String PRODUCT_ID = "PRDT000";
    private static final String SOCIAL_ORDER_ID = CommonUtil.randomId();
    private static final String PURCHASE_TOKEN = CommonUtil.randomId();

    @Nested
    @DisplayName("주문 소비 단위 테스트")
    class ConsumeOrder {

        @Test
        @DisplayName("등록된 주문을 소비 처리 한다.")
        void consumeOrder() {
            // arrange
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            // act
            inAppOrder.consume();

            // assert
            assertEquals(OrderTypeCode.CONSUMED, inAppOrder.getOrderTypeCode());
        }

        @Test
        @DisplayName("이미 소비된 인앱 상품 주문인 경우 예외가 발생한다.")
        void alreadyConsumedInAppOrder() {
            // arrange
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            // act & assert
            assertThrows(AlreadyConsumedInAppOrderException.class, inAppOrder::consume);
        }

        @Test
        @DisplayName("구매가 완료되지 않은 주문인 경우 예외가 발생한다.")
        void notPayedInAppOrder() {
            // arrange
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PENDING;

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            // act & assert
            assertThrows(PaymentNotCompletedInAppOrderException.class, inAppOrder::consume);
        }

        @Test
        @DisplayName("구매가 취소된 주문인 경우 예외가 발생한다.")
        void payCancelInAppOrder() {
            // arrange
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.CANCEL;

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            // act & assert
            assertThrows(PaymentNotCompletedInAppOrderException.class, inAppOrder::consume);
        }
    }
}