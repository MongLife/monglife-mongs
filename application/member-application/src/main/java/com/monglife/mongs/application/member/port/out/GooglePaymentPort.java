package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.domain.Order;
import com.monglife.mongs.application.member.domain.Product;
import com.monglife.mongs.application.member.port.vo.CreateOrderVo;

import java.util.List;

public interface GooglePaymentPort {

    void consumeOrderPort(Long orderId);

    void createOrderPort(CreateOrderVo createOrderVo);

    List<Order> getConsumedOrdersPort();

    List<Product> getProductsPort();

    Order getOrderPort(Long orderId);
}
