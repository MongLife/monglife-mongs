package com.monglife.mongs.client.google.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.InAppProduct;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.monglife.mongs.client.google.dto.etc.GetInAppProductDto;
import com.monglife.mongs.client.google.exception.InvalidConsumeOrderException;
import com.monglife.mongs.client.google.exception.InvalidGetOrderException;
import com.monglife.mongs.client.google.exception.InvalidGetProductsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private static final String appPackageName = "com.mongs.wear";

    private final AndroidPublisher androidPublisher;


//    public List<GetInAppProductDto> getInAppProducts() {
//
//        try {
//            AndroidPublisher.Inappproducts.List products = androidPublisher.inappproducts().list(appPackageName);
//
//            return products.execute().getInappproduct().stream()
//                    .filter(inAppProduct -> "active".equals(inAppProduct.getStatus()))
//                    .map(inAppProduct -> {
//                        String productId = inAppProduct.getSku().toUpperCase();
//
//                        String productName = inAppProduct.getListings().get("ko-KR").getTitle();
//
//                        double price = priceMicrosToPrice(inAppProduct.getDefaultPrice().getPriceMicros());
//
//                        return GetInAppProductDto.builder()
//                                .productId(productId)
//                                .productName(productName)
//                                .price(price)
//                                .build();
//                    })
//                    .toList();
//
//        } catch (Exception e) {
//            throw new InvalidGetProductsException();
//        }
//    }

    public Double getInAppProductPrice(String productId) {

        try {
            AndroidPublisher.Inappproducts.Get get = androidPublisher.inappproducts().get(appPackageName, productId.toLowerCase());
            InAppProduct product = get.execute();

            log.info("Get inappproduct price: {}", product);

            return priceMicrosToPrice(product.getDefaultPrice().getPriceMicros());

        } catch (Exception e) {
            throw new InvalidGetOrderException(productId);
        }
    }

    public void verityInAppOrder(String productId, String purchaseToken) {

    }

//    public void consumeInAppOrder(Long accountId, Long productOrderId, String purchaseToken) {
//
//        try {
//
//            GetProductOrderDto getProductOrderDto = productOrderService.getProductOrder(productOrderId);
//
//            String productId = getProductOrderDto.getProductId();
//
//            AndroidPublisher.Purchases.Products.Get get = androidPublisher.purchases().products().get(appPackageName, productId.toLowerCase(), purchaseToken);
//            ProductPurchase purchase = get.execute();
//
//            log.info("orderId: {}", purchase.getOrderId());
//            log.info("productId: {}", purchase.getProductId());
//            log.info("purchaseState: {}", purchase.getPurchaseState());
//            log.info("purchaseToken: {}", purchase.getPurchaseToken());
//            log.info("purchaseTimeMillis: {}", purchase.getPurchaseTimeMillis());
//            log.info("purchaseType: {}", purchase.getPurchaseType());
//
//            // TODO: 구매 상태 확인 -> 환불 및 구매 포기한 경우 예외 발생하도록 변경
//
//            productOrderService.consumeProductOrder(productOrderId, purchase.getOrderId());
//
////            String consumeUrl = String.format("%s/%s/purchases/products/%s/tokens/%s:consume", googleApiUrl, appPackageName, productId, purchaseToken);
////            androidPublisher.getRequestFactory().buildPostRequest(new GenericUrl(consumeUrl), null).execute();
//
//            // 주문 소비 실행
//            Integer starPoint = switch (productId) {
//                case "PRDT000" -> 10;
//                case "PRDT001" -> 30;
//                case "PRDT002" -> 50;
//                default -> 0;
//            };
//
//        } catch (Exception e) {
//            throw new InvalidConsumeOrderException(productOrderId, purchaseToken);
//        }
//    }

    private static Double priceMicrosToPrice(String priceMicrosStr) {

        String head = priceMicrosStr.substring(0, priceMicrosStr.length() - 6);
        String tail = priceMicrosStr.substring(priceMicrosStr.length() - 6);

        return Double.parseDouble(head + "." + tail);
    }
}
