package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.CreateOrderVo;
import com.monglife.mongs.domain.member.model.ExchangeStarPointProduct;
import com.monglife.mongs.domain.member.model.Order;

import java.util.Optional;

public interface OrderPersistencePort {

    Optional<Order> createOrderPort(CreateOrderVo createOrderVo);

    Optional<Order> saveOrderPort(Order order);
}
