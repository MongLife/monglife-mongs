package com.monglife.mongs.app.user.store.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.InAppProduct;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.monglife.mongs.app.user.store.dto.etc.GetProductDto;
import com.monglife.mongs.app.user.store.exception.InvalidConsumeOrderException;
import com.monglife.mongs.app.user.store.exception.InvalidCreateOrderException;
import com.monglife.mongs.app.user.store.exception.InvalidGetProductsException;
import com.monglife.mongs.domain.member.dto.etc.GetProductOrderDto;
import com.monglife.mongs.domain.member.service.ProductOrderService;
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

    @Value("${application.app-package-name}")
    private String appPackageName;

    @Value("${application.google.url}")
    private String googleApiUrl;

    private final AndroidPublisher androidPublisher;

    private final ProductOrderService productOrderService;

    @Transactional(readOnly = true)
    public List<GetProductDto> getProducts() {

        try {
            AndroidPublisher.Inappproducts.List products = androidPublisher.inappproducts().list(appPackageName);

            return products.execute().getInappproduct().stream()
                    .filter(inAppProduct -> "active".equals(inAppProduct.getStatus()))
                    .map(inAppProduct -> {
                        String productId = inAppProduct.getSku().toUpperCase();

                        String productName = inAppProduct.getListings().get("ko-KR").getTitle();

                        double price = this.priceMicrosToPrice(inAppProduct.getDefaultPrice().getPriceMicros());

                        return GetProductDto.builder()
                                .productId(productId)
                                .productName(productName)
                                .price(price)
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

            AndroidPublisher.Inappproducts.Get get = androidPublisher.inappproducts().get(appPackageName, productId.toLowerCase());
            InAppProduct product = get.execute();

            double price = this.priceMicrosToPrice(product.getDefaultPrice().getPriceMicros());

            return productOrderService.createProductOrder(accountId, productId, price);

        } catch (Exception e) {
            throw new InvalidCreateOrderException(productId);
        }

    }

    @Transactional
    public void consumeOrder(Long productOrderId, String purchaseToken) {

        try {

            GetProductOrderDto getProductOrderDto = productOrderService.getProductOrder(productOrderId);

            String productId = getProductOrderDto.getProductId().toLowerCase();

            AndroidPublisher.Purchases.Products.Get get = androidPublisher.purchases().products().get(appPackageName, productId, purchaseToken);
            ProductPurchase purchase = get.execute();

            log.info("orderId: {}", purchase.getOrderId());
            log.info("productId: {}", purchase.getProductId());
            log.info("purchaseState: {}", purchase.getPurchaseState());
            log.info("purchaseToken: {}", purchase.getPurchaseToken());
            log.info("purchaseTimeMillis: {}", purchase.getPurchaseTimeMillis());
            log.info("purchaseType: {}", purchase.getPurchaseType());

            // TODO: 구매 상태 확인 -> 환불 및 구매 포기한 경우 예외 발생하도록 변경

            productOrderService.consumeProductOrder(productOrderId, purchase.getOrderId());

            String consumeUrl = String.format("%s/%s/purchases/products/%s/tokens/%s:consume", googleApiUrl, appPackageName, productId, purchaseToken);

            androidPublisher.getRequestFactory().buildPostRequest(new GenericUrl(consumeUrl), null).execute();

        } catch (Exception e) {

            throw new InvalidConsumeOrderException(productOrderId, purchaseToken);
        }
    }

    private Double priceMicrosToPrice(String priceMicrosStr) {

        String head = priceMicrosStr.substring(0, priceMicrosStr.length() - 6);
        String tail = priceMicrosStr.substring(priceMicrosStr.length() - 6);

        return Double.parseDouble(head + "." + tail);
    }
}
