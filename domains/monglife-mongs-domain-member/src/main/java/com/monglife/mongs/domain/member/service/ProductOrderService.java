package com.monglife.mongs.domain.member.service;

import com.monglife.mongs.domain.member.vo.ProductOrderVo;
import com.monglife.mongs.domain.member.entity.MemberEntity;
import com.monglife.mongs.domain.member.entity.ProductOrderEntity;
import com.monglife.mongs.domain.member.exception.NotExistsMemberException;
import com.monglife.mongs.domain.member.exception.NotExistsProductCodeException;
import com.monglife.mongs.domain.member.exception.NotExistsProductOrderException;
import com.monglife.mongs.domain.member.repository.ComnCodeRepository;
import com.monglife.mongs.domain.member.repository.LockProductOrderRepository;
import com.monglife.mongs.domain.member.repository.MemberRepository;
import com.monglife.mongs.domain.member.repository.ProductOrderRepository;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOrderService {

    // 상품 그룹 코드
    @Value("${application.service.product-order.product-group-code}")
    private String PRODUCT_GROUP_CODE;

    private final ComnCodeRepository comnCodeRepository;

    private final MemberRepository memberRepository;

    private final ProductOrderRepository productOrderRepository;

    private final LockProductOrderRepository lockProductOrderRepository;

    /**
     * 구매 가능한 상품 ID 목록 조회
     * @return 상품 ID 목록 (UpperCase)
     */
    @Transactional
    public List<String> getProductIds() {

        return comnCodeRepository.findByGroupCode(PRODUCT_GROUP_CODE).stream()
                .map(ComnCodeEntity::getCode)
                .toList();
    }

    /**
     * 상품 주문 내역 조회
     * @param productOrderId 상품 주문 ID
     * @return 상품 주문 Vo
     */
    @Transactional(readOnly = true)
    public ProductOrderVo getProductOrder(Long productOrderId) {

        ProductOrderEntity productOrderEntity = productOrderRepository.findByProductOrderId(productOrderId)
                .orElseThrow(() -> new NotExistsProductOrderException(productOrderId));

        return ProductOrderVo.builder()
                .accountId(productOrderEntity.getMember().getAccountId())
                .productId(productOrderEntity.getComn().getCode())
                .orderId(productOrderEntity.getOrderId())
                .purchaseToken(productOrderEntity.getPurchaseToken())
                .build();
    }

    /**
     * 소비 완료 처리된 상품 주문 내역 목록 조회
     * @param orderIds 플랫폼 주문 ID 목록
     * @return 상품 주문 Vo 목록
     */
    @Transactional(readOnly = true)
    public List<ProductOrderVo> getConsumedProductOrders(List<String> orderIds) {

        List<ProductOrderEntity> productOrderEntities = productOrderRepository.findAllByOrderIdIn(orderIds).stream()
                .filter(ProductOrderEntity::isConsumed)
                .toList();


        return productOrderEntities.stream()
                .map(productOrderEntity -> ProductOrderVo.builder()
                        .accountId(productOrderEntity.getMember().getAccountId())
                        .productId(productOrderEntity.getComn().getCode())
                        .orderId(productOrderEntity.getOrderId())
                        .purchaseToken(productOrderEntity.getPurchaseToken())
                        .build())
                .toList();
    }

    /**
     * 상품 주문 등록
     * @param accountId 계정 ID
     * @param productId 상품 ID
     * @param price 상품 가격
     * @param orderId 플랫폼 주문 ID
     * @param purchaseToken 플랫폼 주문 검증 토큰
     * @return 상품 주문 ID
     */
    @Transactional
    public Long createProductOrder(Long accountId, String productId, Double price, String orderId, String purchaseToken) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(productId)
                .orElseThrow(() -> new NotExistsProductCodeException(productId));

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        ProductOrderEntity productOrderEntity = productOrderRepository.findByOrderId(orderId)
                .orElseGet(() -> productOrderRepository.save(
                        ProductOrderEntity.builder()
                                .member(memberEntity)
                                .comn(comnCodeEntity)
                                .price(price)
                                .orderId(orderId)
                                .purchaseToken(purchaseToken)
                                .build()));

        return productOrderEntity.getProductOrderId();
    }

    /**
     * 상품 주문 소비 처리
     * @param productOrderId 상품 주문 ID
     */
    @Transactional
    public void consumeProductOrder(Long productOrderId) {

        ProductOrderEntity productOrderEntity = lockProductOrderRepository.findByProductOrderId(productOrderId)
                .orElseThrow(() -> new NotExistsProductOrderException(productOrderId));

        if (!productOrderEntity.isConsumed()) {
            productOrderEntity.consume();
        }
    }
}
