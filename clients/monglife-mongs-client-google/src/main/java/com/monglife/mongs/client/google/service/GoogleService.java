package com.monglife.mongs.client.google.service;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.InAppProduct;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.monglife.mongs.client.google.exception.ConsumeOrderException;
import com.monglife.mongs.client.google.exception.GetProductsException;
import com.monglife.mongs.client.google.exception.GetOrderException;
import com.monglife.mongs.client.google.vo.InAppOrderVo;
import com.monglife.mongs.client.google.vo.InAppProductVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private static final String appPackageName = "com.mongs.wear";

    private final AndroidPublisher androidPublisher;

    public InAppProductVo getInAppProduct(String productId) {

        try {
            AndroidPublisher.Inappproducts.Get get = androidPublisher.inappproducts().get(appPackageName, productId.toLowerCase());
            InAppProduct product = get.execute();

            String productName = product.getSku();
            double price = priceMicrosToPrice(product.getDefaultPrice().getPriceMicros());

            return InAppProductVo.builder()
                    .productId(productId)
                    .productName(productName)
                    .price(price)
                    .build();

        } catch (Exception e) {
            throw new GetProductsException(productId);
        }
    }

    public InAppOrderVo getInAppOrder(String productId, String orderId, String purchaseToken) {

        try {
            AndroidPublisher.Purchases.Products.Get get = androidPublisher.purchases().products().get(appPackageName, productId.toLowerCase(), purchaseToken);
            ProductPurchase purchase = get.execute();

            String verityOrderId = purchase.getOrderId();
            Integer verityPurchaseState = purchase.getPurchaseState();              // 구매 여부 ==> 0 구매함, 1 취소됨, 2 대기중
            Integer verityConsumptionState = purchase.getConsumptionState();        // 소비 여부 ==> 0 소비 되지 않음, 1 소비됨
            LocalDateTime verityPurchasedAt = Instant.ofEpochMilli(purchase.getPurchaseTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            return InAppOrderVo.builder()
                    .orderId(verityOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .isPurchase(verityPurchaseState == 0)
                    .isConsume(verityConsumptionState == 1)
                    .purchasedAt(verityPurchasedAt)
                    .build();

        } catch (Exception e) {
            throw new GetOrderException(productId, orderId, purchaseToken);
        }
    }

    public void consumeOrder(String productId, String purchaseToken) {

        try {
            AndroidPublisher.Purchases.Products.Consume consume = androidPublisher.purchases().products().consume(appPackageName, productId.toLowerCase(), purchaseToken);
            consume.execute();
        } catch (Exception e) {
            throw new ConsumeOrderException(productId, purchaseToken);
        }
    }

    private static Double priceMicrosToPrice(String priceMicrosStr) {

        String head = priceMicrosStr.substring(0, priceMicrosStr.length() - 6);
        String tail = priceMicrosStr.substring(priceMicrosStr.length() - 6);

        return Double.parseDouble(head + "." + tail);
    }
}
