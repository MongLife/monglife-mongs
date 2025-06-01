package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.ConsumeOrderCommand;
import com.monglife.mongs.application.member.port.in.command.CreateOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetConsumedOrderCommand;
import com.monglife.mongs.domain.member.model.Order;
import com.monglife.mongs.domain.member.model.InAppProduct;

import java.util.List;

public interface StoreUseCase {

    /**
     * 주문 등록
     */
    void createOrderUseCase(CreateOrderCommand command);

    /**
     * 주문 소비
     */
    Order consumeOrderUseCase(ConsumeOrderCommand command);

    /**
     * 인앱 상품 목록 조회
     */
    List<InAppProduct> getProductsUseCase();

    /**
     * 주문 소비 내역 목록 조회
     */
    List<Order> getConsumedOrderUseCase(GetConsumedOrderCommand command);
}
