package com.monglife.mongs.adapter.out.google.payment.service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.monglife.mongs.adapter.out.google.payment.utills.PaymentUtil;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.domain.member.enums.OrderPurchaseTypeCode;
import com.monglife.mongs.domain.member.enums.OrderTypeCode;
import com.monglife.mongs.domain.member.model.InAppOrder;
import com.monglife.mongs.domain.member.model.InAppProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GooglePaymentService implements GooglePaymentPort {

    @Value("${google.app-package-name}")
    private String appPackageName;

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
            var get = androidPublisher.purchases().products().get(appPackageName, productId.toLowerCase(), purchaseToken);
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

        } catch (Exception e){
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
                    .consume(appPackageName, inAppOrder.getProductId().toLowerCase(), inAppOrder.getPurchaseToken())
                    .execute();

            return this.getInAppOrderPort(inAppOrder.getProductId(), inAppOrder.getSocialOrderId(), inAppOrder.getPurchaseToken());

        } catch (Exception e) {;
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
            var get = androidPublisher.inappproducts().get(appPackageName, productId.toLowerCase());
            var inAppProduct = get.execute();

            var inAppProductListing = inAppProduct.getListings().get("ko-KR");

            return Optional.of(InAppProduct.builder()
                    .productId(inAppProduct.getSku().toUpperCase())
                    .productName(inAppProductListing != null ? inAppProductListing.getTitle() : "-")
                    .price(PaymentUtil.priceMicrosToPrice(inAppProduct.getDefaultPrice().getPriceMicros()))
                    .build());

        } catch (Exception e) {
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
                    .list(appPackageName)
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
            return List.of();
        }
    }
}
