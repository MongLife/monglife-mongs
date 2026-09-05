package com.monglife.mongs.adapter.out.google.payment.service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.monglife.mongs.adapter.out.google.payment.utills.PaymentUtil;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.domain.member.enums.OrderPurchaseTypeCode;
import com.monglife.mongs.domain.member.enums.OrderTypeCode;
import com.monglife.mongs.domain.member.model.InAppOrder;
import com.monglife.mongs.domain.member.model.InAppProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePaymentService implements GooglePaymentPort {

    @Value("${google.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final AndroidPublisher androidPublisher;

    /**
     * 인앱 상품 주문 조회
     * @param productId 인앱 상품 ID
     * @param socialOrderId 인앱 상품 주문 ID
     * @param purchaseToken 인앱 상품 결제 토큰
     * @return 인앱 상품 주문 도메인 객체
     */
    @Override
    public Optional<InAppOrder> getInAppOrderPort(String productId, String socialOrderId, String purchaseToken) {

        try {
            var get = androidPublisher.purchases().products().get(APP_PACKAGE_NAME, productId.toLowerCase(), purchaseToken);
            var purchase = get.execute();

            OrderPurchaseTypeCode orderPurchaseTypeCode = switch (purchase.getPurchaseState()) {
                case 0 -> OrderPurchaseTypeCode.PAYED;      // 0 : 구매함
                case 1 -> OrderPurchaseTypeCode.CANCEL;     // 1 : 취소됨
                default -> OrderPurchaseTypeCode.PENDING;   // 2 : 대기중
            };

            OrderTypeCode orderTypeCode;
            if (purchase.getConsumptionState() == 0) {
                orderTypeCode = OrderTypeCode.ORDERED;      // 0 : 소비 전
            } else {
                orderTypeCode = OrderTypeCode.CONSUMED;     // 1 : 소비 완료
            }

            LocalDateTime purchasedAt = Instant.ofEpochMilli(purchase.getPurchaseTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            return Optional.of(InAppOrder.builder()
                    .socialOrderId(purchase.getOrderId())
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(orderTypeCode)
                    .purchasedAt(purchasedAt)
                    .build());

        } catch (Exception e) {
            log.error("인앱 주문 조회 실패 - productId: {}, socialOrderId: {}, purchaseToken: {}",
                    productId, socialOrderId, maskToken(purchaseToken), e);
            return Optional.empty();
        }
    }

    /**
     * 인앱 상품 주문 소비
     */
    @Override
    public Optional<InAppOrder> consumeInAppOrderPort(InAppOrder inAppOrder) {

        try {
            androidPublisher.purchases()
                    .products()
                    .consume(APP_PACKAGE_NAME, inAppOrder.getProductId().toLowerCase(), inAppOrder.getPurchaseToken())
                    .execute();

            /**
             * 재조회하지 않고 인자로 받은 주문을 그대로 돌려준다.
             *
             * 이전에는 consume 직후 getInAppOrderPort 를 한 번 더 호출했는데, 그 재조회가
             * 실패하면 Optional.empty() 가 되어 상위 StoreService 에서
             * InvalidConsumeInAppOrderException 으로 트랜잭션이 롤백됐다.
             * 그런데 Google 쪽은 이미 소비가 끝난 상태라 스타 포인트만 사라지고
             * 재시도해도 AlreadyConsumedInAppOrderException 으로 막혀 영구 손실이 된다.
             *
             * consume 이 성공했다는 사실만으로 소비 완료가 확정되고,
             * 인자로 받은 inAppOrder 는 호출 전에 consume() 으로 이미 CONSUMED 다.
             */
            return Optional.of(inAppOrder);

        } catch (Exception e) {
            log.error("인앱 주문 소비 실패 - productId: {}, socialOrderId: {}, purchaseToken: {}",
                    inAppOrder.getProductId(), inAppOrder.getSocialOrderId(), maskToken(inAppOrder.getPurchaseToken()), e);
            return Optional.empty();
        }
    }

    /**
     * 인앱 상품 조회
     * @param productId 인앱 상품 ID
     * @return 인앱 상품 도메인 객체
     */
    @Override
    public Optional<InAppProduct> getInAppProductPort(String productId) {

        try {
            var get = androidPublisher.inappproducts().get(APP_PACKAGE_NAME, productId.toLowerCase());
            var inAppProduct = get.execute();

            var inAppProductListing = inAppProduct.getListings().get("ko-KR");

            return Optional.of(InAppProduct.builder()
                    .productId(inAppProduct.getSku().toUpperCase())
                    .productName(inAppProductListing != null ? inAppProductListing.getTitle() : "-")
                    .price(PaymentUtil.priceMicrosToPrice(inAppProduct.getDefaultPrice().getPriceMicros()))
                    .build());

        } catch (Exception e) {
            log.error("인앱 상품 조회 실패 - productId: {}", productId, e);
            return Optional.empty();
        }
    }

    /**
     * 인앱 상품 목록 조회
     * @return 인앱 상품 도메인 객체 목록
     */
    @Override
    public List<InAppProduct> getInAppProductsPort() {

        try {
            return androidPublisher.inappproducts()
                    .list(APP_PACKAGE_NAME)
                    .execute()
                    .getInappproduct()
                    .stream()
                    .map(inAppProduct -> {
                        var inAppProductListing = inAppProduct.getListings().get("ko-KR");

                        return InAppProduct.builder()
                                .productId(inAppProduct.getSku().toUpperCase())
                                .productName(inAppProductListing != null ? inAppProductListing.getTitle() : "-")
                                .price(PaymentUtil.priceMicrosToPrice(inAppProduct.getDefaultPrice().getPriceMicros()))
                                .build();

                    })
                    .toList();

        } catch (Exception e) {
            log.error("인앱 상품 목록 조회 실패", e);
            return List.of();
        }
    }

    /**
     * 로그에 남길 purchaseToken 마스킹
     *
     * 토큰 전체는 결제를 식별하는 민감 값이라 앞 8자만 남긴다.
     * 로그에서 같은 주문을 이어 추적하기에는 충분하다.
     */
    private String maskToken(String purchaseToken) {

        if (purchaseToken == null || purchaseToken.length() <= 8) {
            return "****";
        }

        return purchaseToken.substring(0, 8) + "...";
    }
}
