package com.monglife.mongs.app.user.store.service;

import com.monglife.mongs.app.user.store.dto.etc.GetProductDto;
import com.monglife.mongs.app.user.store.exception.AlreadyConsumeOrderException;
import com.monglife.mongs.app.user.store.exception.InvalidConsumeOrderException;
import com.monglife.mongs.client.google.service.GoogleService;
import com.monglife.mongs.client.google.vo.InAppOrderVo;
import com.monglife.mongs.client.google.vo.InAppProductVo;
import com.monglife.mongs.domain.member.vo.ProductOrderVo;
import com.monglife.mongs.domain.member.service.MemberService;
import com.monglife.mongs.domain.member.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final ProductOrderService productOrderService;

    private final MemberService memberService;

    private final GoogleService googleService;

    @Transactional(readOnly = true)
    public List<GetProductDto> getProducts() {
        return productOrderService.getProductIds().stream()
                .map(productId -> GetProductDto.builder()
                        .productId(productId)
                        .build())
                .toList();
    }

    @Transactional
    public Long createProductOrder(Long accountId, String productId, String orderId, String purchaseToken) {

        // 상품 정보 확인
        InAppProductVo productVo = googleService.getInAppProduct(productId);

        Double price = productVo.getPrice();

        // 상품 주문 등록
        return productOrderService.createProductOrder(accountId, productId, price, orderId, purchaseToken);
    }

    @Transactional
    public void consumeProductOrder(Long productOrderId) {

        ProductOrderVo productOrderVo = productOrderService.getProductOrder(productOrderId);

        Long accountId = productOrderVo.getAccountId();
        String productId = productOrderVo.getProductId();
        String orderId = productOrderVo.getOrderId();
        String purchaseToken = productOrderVo.getPurchaseToken();

        // 구매 검증
        InAppOrderVo inAppOrderVo = googleService.getInAppOrder(productId, orderId, purchaseToken);

        // 구매 안함
        if (!inAppOrderVo.getIsPurchase()) {
            throw new InvalidConsumeOrderException(productOrderId, purchaseToken);
        }

        // 소비 완료
        if (inAppOrderVo.getIsConsume()) {
            throw new AlreadyConsumeOrderException(productOrderId, purchaseToken);
        }

        // 주문 소비 실행
        memberService.increaseStarPoint(accountId, switch (productId) {
            case "PRDT000" -> 10;
            case "PRDT001" -> 30;
            case "PRDT002" -> 50;
            default -> 0;
        });

        // 소비 처리
        productOrderService.consumeProductOrder(productOrderId);

        // 구글 소비 처리
        googleService.consumeOrder(productId, purchaseToken);
    }

    @Transactional
    public List<String> getConsumedOrderIds(List<String> orderIds) {

        List<ProductOrderVo> productOrderVos = productOrderService.getConsumedProductOrders(orderIds);

        List<String> consumedOrderIds = productOrderVos.stream()
                .map(ProductOrderVo::getOrderId)
                .toList();

        return orderIds.stream()
                .filter(consumedOrderIds::contains)
                .toList();
    }
}
