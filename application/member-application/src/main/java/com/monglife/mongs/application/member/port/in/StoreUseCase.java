package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.domain.Order;
import com.monglife.mongs.application.member.domain.Product;
import com.monglife.mongs.application.member.port.command.*;

import java.util.List;

public interface StoreUseCase {

    void buyProductUseCase(BuyProductCommand buyProductCommand);

    List<Product> getProductsUseCase(GetProductsCommand getProductsCommand);

    void consumeOrderUseCase(ConsumeOrderCommand consumeOrderCommand);

    List<Order> getConsumedOrderUseCase(GetConsumedOrderCommand getConsumedOrderCommand);
}
