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

    @Value("${application.service.product-order.product-group-code}")
    private String PRODUCT_GROUP_CODE;

    private final ComnCodeRepository comnCodeRepository;

    private final MemberRepository memberRepository;

    private final ProductOrderRepository productOrderRepository;

    private final LockProductOrderRepository lockProductOrderRepository;

    @Transactional
    public List<String> getProductIds() {

        return comnCodeRepository.findByGroupCode(PRODUCT_GROUP_CODE).stream()
                .map(ComnCodeEntity::getCode)
                .toList();
    }

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

    @Transactional
    public void consumeProductOrder(Long productOrderId) {

        ProductOrderEntity productOrderEntity = lockProductOrderRepository.findByProductOrderId(productOrderId)
                .orElseThrow(() -> new NotExistsProductOrderException(productOrderId));

        productOrderEntity.consume();
    }
}
