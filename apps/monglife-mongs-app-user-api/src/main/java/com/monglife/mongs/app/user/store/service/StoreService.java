package com.monglife.mongs.app.user.store.service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.InAppProduct;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.monglife.mongs.app.user.store.dto.etc.GetProductDto;
import com.monglife.mongs.app.user.store.exception.InvalidConsumeOrderException;
import com.monglife.mongs.app.user.store.exception.InvalidCreateOrderException;
import com.monglife.mongs.app.user.store.exception.InvalidGetProductsException;
import com.monglife.mongs.domain.member.repository.ComnCodeRepository;
import com.monglife.mongs.domain.member.service.ProductOrderService;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    @Value("${spring.google.package-name}")
    private String googleApplicationPackageName;

    private final AndroidPublisher androidPublisher;

    private final ProductOrderService productOrderService;

    private final ComnCodeRepository comnCodeRepository;

    @Transactional(readOnly = true)
    public List<GetProductDto> getProducts() {

        try {
            AndroidPublisher.Inappproducts.List products = androidPublisher.inappproducts().list(googleApplicationPackageName);

            return products.execute().getInappproduct().stream()
                    .filter(inAppProduct -> "active".equals(inAppProduct.getStatus()))
                    .map(inAppProduct -> {
                        String productId = inAppProduct.getSku().toUpperCase();

                        String productName = inAppProduct.getListings().get("ko-KR").getTitle();

                        int priceMicros = Integer.parseInt(inAppProduct.getDefaultPrice().getPriceMicros());
                        long price = Math.round(priceMicros / 1000000D);

                        return GetProductDto.builder()
                                .productId(productId)
                                .productName(productName)
                                .price((int) price)
                                .build();
                    })
                    .toList();

        } catch (Exception e) {
            throw new InvalidGetProductsException();
        }
    }

    @Transactional
    public Long createOrder(Long accountId, String productId) {

        try {

            AndroidPublisher.Inappproducts.Get get = androidPublisher.inappproducts().get(googleApplicationPackageName, productId.toLowerCase());
            InAppProduct product = get.execute();

            int priceMicros = Integer.parseInt(product.getDefaultPrice().getPriceMicros());

            long price = Math.round(priceMicros / 1000000D);

            return productOrderService.createProductOrder(accountId, productId, (int) price);

        } catch (Exception e) {
            throw new InvalidCreateOrderException(productId);
        }

    }

    @Transactional
    public void consumeOrder(Long accountId, Long productOrderId, String productId, String purchaseToken) {

        try {
            AndroidPublisher.Purchases.Products.Get get = androidPublisher.purchases().products().get(googleApplicationPackageName, productId.toLowerCase(), purchaseToken);
            ProductPurchase purchase = get.execute();

            log.info("orderId: {}", purchase.getOrderId());
            log.info("productId: {}", purchase.getProductId());
            log.info("purchaseState: {}", purchase.getPurchaseState());
            log.info("purchaseToken: {}", purchase.getPurchaseToken());
            log.info("purchaseTimeMillis: {}", purchase.getPurchaseTimeMillis());
            log.info("purchaseType: {}", purchase.getPurchaseType());

            // TODO: 구매 상태 확인 -> 환불 및 구매 포기한 경우 예외 발생하도록 변경

            productOrderService.consumeProductOrder(productOrderId, purchase.getOrderId());

        } catch (Exception e) {

            e.printStackTrace();

            throw new InvalidConsumeOrderException(productId, purchaseToken);
        }
    }
}
