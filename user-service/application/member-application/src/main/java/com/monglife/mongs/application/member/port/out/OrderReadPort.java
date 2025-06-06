package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.domain.member.model.ExchangeStarPointProduct;
import com.monglife.mongs.domain.member.model.Order;

import java.util.Optional;

public interface OrderReadPort {

    Boolean isExistsOrderByAccountIdAndSocialOrderIdPort(Long accountId, String socialOrderId);

    Optional<ExchangeStarPointProduct> getExchangeStarPointProductPort(String productId);

    Optional<Order> getOrderBySocialOrderIdPort(String socialOrderId);
}
