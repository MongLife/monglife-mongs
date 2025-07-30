package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.mongs.adapter.out.member.persistence.entity.ExchangeStarPointProductEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.OrderEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.ExchangeStarPointProductRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.OrderRepository;
import com.monglife.mongs.application.member.port.out.OrderReadPort;
import com.monglife.mongs.domain.member.model.ExchangeStarPointProduct;
import com.monglife.mongs.domain.member.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderReadService implements OrderReadPort {

    private final OrderRepository orderRepository;

    private final ExchangeStarPointProductRepository exchangeStarPointProductRepository;

    /**
     * 주문 존재 여부 조회
     * @param accountId 회원 ID
     * @param socialOrderId 인앱 주문 ID
     * @return 주문 존재 여부
     */
    @Override
    @Transactional
    public Boolean isExistsOrderByAccountIdAndSocialOrderIdPort(Long accountId, String socialOrderId) {
        return orderRepository.existsByAccountIdAndSocialOrderId(accountId, socialOrderId);
    }

    /**
     * 스타 포인트 환전 상품 조회
     * @param productId 인앱 상품 ID
     * @return 스타 포인트 환전 상품 도메인 옵셔널 객체
     */
    @Override
    @Transactional
    public Optional<ExchangeStarPointProduct> getExchangeStarPointProductPort(String productId) {
        return exchangeStarPointProductRepository.findByProductId(productId).map(ExchangeStarPointProductEntity::toDomain);
    }

    /**
     * 인앱 주문 ID 기준 주문 조회
     * @param socialOrderId 인앱 주문 ID
     * @return 주문 도메인 옵셔널 객체
     */
    @Override
    @Transactional
    public Optional<Order> getOrderBySocialOrderIdPort(String socialOrderId) {
        return orderRepository.findBySocialOrderId(socialOrderId).map(OrderEntity::toDomain);
    }
}
