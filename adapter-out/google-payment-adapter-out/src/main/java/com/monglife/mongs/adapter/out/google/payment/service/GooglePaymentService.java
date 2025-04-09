package com.monglife.mongs.adapter.out.google.payment.service;

import com.monglife.mongs.application.member.domain.Order;
import com.monglife.mongs.application.member.domain.Product;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.application.member.port.vo.CreateOrderVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GooglePaymentService implements GooglePaymentPort {

    @Override
    public void consumeOrderPort(Long orderId) {

    }

    @Override
    public void createOrderPort(CreateOrderVo createOrderVo) {

    }

    @Override
    public List<Order> getConsumedOrdersPort() {
        return List.of();
    }

    @Override
    public List<Product> getProductsPort() {
        return List.of();
    }

    @Override
    public Order getOrderPort(Long orderId) {
        return null;
    }
}
