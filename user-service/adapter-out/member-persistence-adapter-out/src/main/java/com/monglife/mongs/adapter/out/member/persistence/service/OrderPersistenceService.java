package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.ExchangeStarPointProductEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.OrderEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.ComnCodeRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.ExchangeStarPointProductRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.OrderRepository;
import com.monglife.mongs.application.member.port.out.OrderPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreateOrderVo;
import com.monglife.mongs.domain.member.model.ExchangeStarPointProduct;
import com.monglife.mongs.domain.member.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderPersistenceService implements OrderPersistencePort {

    private final ComnCodeRepository comnCodeRepository;

    private final ExchangeStarPointProductRepository exchangeStarPointProductRepository;

    private final OrderRepository orderRepository;

    /**
     * 주문 존재 여부 조회
     * @param accountId 회원 ID
     * @param socialOrderId 인앱 주문 ID
     * @return 주문 존재 여부
     */
    @Override
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
     * 주문 등록
     * @return 주문 도메인 옵셔널 객체
     */
    @Override
    @Transactional
    public Optional<Order> createOrderPort(CreateOrderVo createOrderVo) {

        Optional<ComnCodeEntity> comnCodeEntityOptional = comnCodeRepository.findById(createOrderVo.getProductId());

        if (comnCodeEntityOptional.isPresent()) {
            OrderEntity orderEntity = OrderEntity.builder()
                    .accountId(createOrderVo.getAccountId())
                    .productType(comnCodeEntityOptional.get())
                    .price(createOrderVo.getPrice())
                    .socialOrderId(createOrderVo.getSocialOrderId())
                    .purchaseToken(createOrderVo.getPurchaseToken())
                    .build();

            return Optional.of(orderRepository.save(orderEntity).toDomain());

        } else {
            return Optional.empty();
        }
    }

    /**
     * 주문 수정
     * @param order 주문 도메인 객체
     * @return 주문 도메인 옵셔널 객체
     */
    @Override
    @Transactional
    public Optional<Order> saveOrderPort(Order order) {

        Optional<ComnCodeEntity> comnCodeEntityOptional = comnCodeRepository.findById(order.getProductId());
        Optional<OrderEntity> orderEntityOptional = orderRepository.findById(order.getOrderId());

        if (comnCodeEntityOptional.isPresent() && orderEntityOptional.isPresent()) {
            OrderEntity orderEntity = orderEntityOptional.get();
            orderEntity.update(order, comnCodeEntityOptional.get());

            return Optional.of(orderRepository.save(orderEntity).toDomain());

        } else {
            return Optional.empty();
        }
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
