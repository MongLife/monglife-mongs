package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.ConsumeOrderCommand;
import com.monglife.mongs.application.member.port.in.command.CreateOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetConsumedOrderCommand;
import com.monglife.mongs.domain.model.Order;
import com.monglife.mongs.domain.model.InAppProduct;
import com.monglife.mongs.domain.model.Player;

import java.util.List;

public interface StoreUseCase {

    void createOrderUseCase(CreateOrderCommand command);

    Order consumeOrderUseCase(ConsumeOrderCommand command);

    List<InAppProduct> getProductsUseCase();

    List<Order> getConsumedOrderUseCase(GetConsumedOrderCommand command);
}
