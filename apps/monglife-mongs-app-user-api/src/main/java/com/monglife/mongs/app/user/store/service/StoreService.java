package com.monglife.mongs.app.user.store.service;

import com.monglife.mongs.app.user.store.dto.etc.GetProductDto;
import com.monglife.mongs.client.google.service.GoogleService;
import com.monglife.mongs.domain.member.dto.etc.GetProductOrderDto;
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
    public Long createProductOrder(Long accountId, String productId, String purchaseToken) {

        // 인앱 상품 현재 가격
        double price = googleService.getInAppProductPrice(productId);

        // 상품 주문 등록
        return productOrderService.createProductOrder(accountId, productId, price, purchaseToken);
    }

    @Transactional
    public void consumeProductOrder(Long productOrderId, String purchaseToken) {

        GetProductOrderDto getProductOrderDto = productOrderService.getProductOrder(productOrderId);

        Long accountId = getProductOrderDto.getAccountId();
        String productId = getProductOrderDto.getProductId();

        // 구매 검증
        googleService.verityInAppOrder(productId, purchaseToken);

        // 주문 소비 실행
        memberService.increaseStarPoint(accountId, switch (productId) {
            case "PRDT000" -> 10;
            case "PRDT001" -> 30;
            case "PRDT002" -> 50;
            default -> 0;
        });

        // 소비 처리
        productOrderService.consumeProductOrder(productOrderId);
    }
}
