package com.monglife.mongs.adapter.out.google.payment.service;

import com.monglife.mongs.adapter.out.google.payment.config.AdapterOutGooglePaymentConfig;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.domain.member.enums.OrderPurchaseTypeCode;
import com.monglife.mongs.domain.member.enums.OrderTypeCode;
import com.monglife.mongs.domain.member.model.InAppOrder;
import com.monglife.mongs.domain.member.model.InAppProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = { AdapterOutGooglePaymentConfig.class })
class GooglePaymentServiceTest {

    private final GooglePaymentPort googlePaymentPort;

    @Autowired
    public GooglePaymentServiceTest(GooglePaymentPort googlePaymentPort) {
        this.googlePaymentPort = googlePaymentPort;
    }

    @Nested
    @DisplayName("인앱 상품 주문 조회 단위 테스트")
    class GetInAppOrderPort {

        @Test
        @DisplayName("구글 플레이 API 를 호출하여 결제 완료된 인앱 상품 주문을 조회 한다.")
        void getInAppOrderIsPayed() {
            // arrange
            String productId = "PRDT000";
            String socialOrderId = "GPA.3322-2924-7092-01384";
            String purchaseToken = "eachhdplenbklhmcopnonind.AO-J1OzX2OwMIxVIjggklKnb69c-nUFkeS_lIVrcrqLXt9ZY2a9kSTF9jdGjtgnIBnGGU3SLs30gPqwW_J-xrxqucseX7zbdGQ";

            // act
            Optional<InAppOrder> inAppOrderOptional = googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken);

            // assert
            assertTrue(inAppOrderOptional.isPresent());
            assertEquals(socialOrderId, inAppOrderOptional.get().getSocialOrderId());
            assertEquals(productId, inAppOrderOptional.get().getProductId());
            assertEquals(purchaseToken, inAppOrderOptional.get().getPurchaseToken());
            assertEquals(OrderPurchaseTypeCode.PAYED, inAppOrderOptional.get().getOrderPurchaseTypeCode());
            assertEquals(OrderTypeCode.CONSUMED, inAppOrderOptional.get().getOrderTypeCode());
            assertNotNull(inAppOrderOptional.get().getPurchasedAt());
        }

        @Test
        @DisplayName("구글 플레이 API 를 호출하여 결제 취소된 인앱 상품 주문을 조회 한다.")
        void getInAppOrderIsCanceled() {
            // arrange
            String productId = "PRDT001";
            String socialOrderId = "GPA.3329-2982-1082-96496";
            String purchaseToken = "hbchocjcfbnfbgmajpkhjgpp.AO-J1Oyp19UoLVtJ9lunRGMVIdgMMVhR2z_NSUYwlNiYdBszRyCnQ0PyzgqzlcFArrIEHvaiyJ8Qvoi8MuTSaw40YT6xCio3Cg";

            // act
            Optional<InAppOrder> inAppOrderOptional = googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken);

            // assert
            assertTrue(inAppOrderOptional.isPresent());
            assertEquals(socialOrderId, inAppOrderOptional.get().getSocialOrderId());
            assertEquals(productId, inAppOrderOptional.get().getProductId());
            assertEquals(purchaseToken, inAppOrderOptional.get().getPurchaseToken());
            assertEquals(OrderPurchaseTypeCode.CANCEL, inAppOrderOptional.get().getOrderPurchaseTypeCode());
            assertEquals(OrderTypeCode.ORDERED, inAppOrderOptional.get().getOrderTypeCode());
            assertNotNull(inAppOrderOptional.get().getPurchasedAt());
        }

        @Test
        @DisplayName("인앱 상품 주문이 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void notExistsInAppOrder() {
            // arrange
            String productId = "PRDT002";
            String socialOrderId = "GPA.0000-0000-0000-00000";
            String purchaseToken = "-";

            // act
            Optional<InAppOrder> inAppOrderOptional = googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken);

            // assert
            assertTrue(inAppOrderOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("인앱 상품 조회 단위 테스트")
    class GetInAppProductPort {

        @Test
        @DisplayName("구글 플레이 API 를 호출하여 인앱 상품을 조회 한다.")
        void getInAppProduct() {
            // arrange
            String productId = "PRDT000";

            // act
            Optional<InAppProduct> inAppProductOptional = googlePaymentPort.getInAppProductPort(productId);

            // assert
            assertTrue(inAppProductOptional.isPresent());
            assertEquals(productId, inAppProductOptional.get().getProductId());
        }
    }

    @Nested
    @DisplayName("인앱 상품 목록 조회 단위 테스트")
    class GetInAppProductsPort {

        @Test
        @DisplayName("구글 플레이 API 를 호출하여 인앱 상품 목록을 조회 한다.")
        void getInAppProducts() {
            // act
            List<InAppProduct> inAppProducts = googlePaymentPort.getInAppProductsPort();

            // assert
            assertFalse(inAppProducts.isEmpty());
        }
    }
}