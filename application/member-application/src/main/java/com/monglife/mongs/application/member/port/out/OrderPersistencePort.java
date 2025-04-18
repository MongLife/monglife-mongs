package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.CreateOrderVo;
import com.monglife.mongs.domain.model.ExchangeStarPointProduct;
import com.monglife.mongs.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderPersistencePort {

    Optional<ExchangeStarPointProduct> getExchangeStarPointProductPort(String productId);

    Optional<Order> createOrderPort(CreateOrderVo createOrderVo);

    Optional<Order> saveOrderPort(Order order);

    List<Order> getConsumedOrdersPort(Long accountId);

    Optional<Order> getOrderBySocialOrderIdPort(String socialOrderId);
}
