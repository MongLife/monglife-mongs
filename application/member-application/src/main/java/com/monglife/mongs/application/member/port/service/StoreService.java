package com.monglife.mongs.application.member.port.service;

import com.monglife.mongs.application.member.domain.Order;
import com.monglife.mongs.application.member.domain.Product;
import com.monglife.mongs.application.member.port.command.*;
import com.monglife.mongs.application.member.port.in.StoreUseCase;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService implements StoreUseCase {

    private final MemberPersistencePort memberPersistencePort;

    private final MemberEventPort memberEventPort;

    private final GooglePaymentPort googlePaymentPort;

    @Override
    public void buyProductUseCase(BuyProductCommand buyProductCommand) {

    }

    @Override
    public List<Product> getProductsUseCase(GetProductsCommand getProductsCommand) {
        return List.of();
    }

    @Override
    public void consumeOrderUseCase(ConsumeOrderCommand consumeOrderCommand) {

    }

    @Override
    public List<Order> getConsumedOrderUseCase(GetConsumedOrderCommand getConsumedOrderCommand) {
        return List.of();
    }
}
