package com.monglife.mongs.application.member.port.in.service;

import com.monglife.mongs.application.member.port.exception.*;
import com.monglife.mongs.application.member.port.in.StoreUseCase;
import com.monglife.mongs.application.member.port.in.command.ConsumeOrderCommand;
import com.monglife.mongs.application.member.port.in.command.CreateOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetConsumedOrderCommand;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.application.member.port.out.OrderPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreateOrderVo;
import com.monglife.mongs.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService implements StoreUseCase {

    private final MemberPersistencePort memberPersistencePort;

    private final OrderPersistencePort orderPersistencePort;

    private final MemberPublishPort memberPublishPort;

    private final GooglePaymentPort googlePaymentPort;

    /**
     * 주문 등록
     */
    @Override
    @Transactional
    public void createOrderUseCase(CreateOrderCommand command) {

        if (!orderPersistencePort.isExistsOrderByAccountIdAndSocialOrderIdPort(command.getAccountId(), command.getSocialOrderId())) {
            // 인앱 상품 존재 여부 확인
            InAppProduct inAppProduct = googlePaymentPort.getInAppProductPort(command.getProductId())
                    .orElseThrow(NotExistsInAppProductException::new);

            // 인앱 상품 주문 등록
            orderPersistencePort.createOrderPort(CreateOrderVo.builder()
                            .accountId(command.getAccountId())
                            .productId(command.getProductId())
                            .price(inAppProduct.getPrice())
                            .socialOrderId(command.getSocialOrderId())
                            .purchaseToken(command.getPurchaseToken())
                            .build())
                    .orElseThrow(InvalidCreateOrderException::new);
        }
    }

    /**
     * 주문 소비
     */
    @Override
    @Transactional
    public Order consumeOrderUseCase(ConsumeOrderCommand command) {

        // 주문 정보 조회
        Order order = orderPersistencePort.getOrderBySocialOrderIdPort(command.getSocialOrderId())
                .orElseThrow(NotExistsOrderException::new);

        // 환전 상품 정보 조회
        ExchangeStarPointProduct product = orderPersistencePort.getExchangeStarPointProductPort(order.getProductId())
                .orElseThrow(NotExistsExchangeStarPointProductException::new);

        // 플레이어 정보 조회
        Player player = memberPersistencePort.getPlayerPort(order.getAccountId())
                .orElseThrow(NotExistsPlayerException::new);

        // 인앱 상품 주문 조회
        InAppOrder inAppOrder = googlePaymentPort.getInAppOrderPort(order.getProductId(), order.getSocialOrderId(), order.getPurchaseToken())
                .orElseThrow(NotExistsInAppOrderException::new);

        // 스타 포인트 증가
        player.increaseStarPoint(product.getStarPoint());
        memberPersistencePort.savePlayerPort(player)
                .orElseThrow(NotExistsPlayerException::new);

        // 인앱 상품 주문 소비
        inAppOrder.consume();
        googlePaymentPort.consumeInAppOrderPort(inAppOrder)
                .orElseThrow(InvalidConsumeInAppOrderException::new);

        // 스타 포인트 비동기 응답
        memberPublishPort.publishStarPointPort(player);

        return order;
    }

    /**
     * 인앱 상품 목록 조회
     */
    @Override
    @Transactional
    public List<InAppProduct> getProductsUseCase() {
        return googlePaymentPort.getInAppProductsPort();
    }

    /**
     * 주문 소비 내역 목록 조회
     */
    @Override
    @Transactional
    public List<Order> getConsumedOrderUseCase(GetConsumedOrderCommand command) {

        List<Order> orders = new ArrayList<>();

        command.getSocialOrderIds().forEach(socialOrderId ->
            orderPersistencePort.getOrderBySocialOrderIdPort(socialOrderId).ifPresent(order ->
                googlePaymentPort.getInAppOrderPort(order.getProductId(), order.getSocialOrderId(), order.getPurchaseToken()).ifPresent(inAppOrder -> {
                    if (inAppOrder.isConsumed()) {
                        orders.add(order);
                    }
                })
            )
        );

        return orders;
    }
}
