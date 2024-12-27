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
        return googleService.getInAppProducts().stream()
                .map(getInAppProductDto -> GetProductDto.builder()
                        .productId(getInAppProductDto.getProductId())
                        .productName(getInAppProductDto.getProductName())
                        .price(getInAppProductDto.getPrice())
                        .build())
                .toList();
    }

    @Transactional
    public Long createOrder(Long accountId, String productId) {

        double price = googleService.getInAppOrder(productId);

        return productOrderService.createProductOrder(accountId, productId, price);
    }

    @Transactional
    public void consumeOrder(Long accountId, Long productOrderId, String purchaseToken) {

        GetProductOrderDto getProductOrderDto = productOrderService.getProductOrder(productOrderId);

        String productId = getProductOrderDto.getProductId();

        // 주문 소비 실행
        Integer starPoint = switch (productId) {
            case "PRDT000" -> 10;
            case "PRDT001" -> 30;
            case "PRDT002" -> 50;
            default -> 0;
        };

        memberService.increaseStarPoint(accountId, starPoint);
    }
}
