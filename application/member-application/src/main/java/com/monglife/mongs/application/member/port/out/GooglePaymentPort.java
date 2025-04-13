package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.domain.model.InAppOrder;
import com.monglife.mongs.domain.model.Order;
import com.monglife.mongs.domain.model.InAppProduct;

import java.util.List;
import java.util.Optional;

public interface GooglePaymentPort {

    Optional<InAppOrder> getInAppOrderPort(Order order);

    void consumeInAppOrderPort(Order order);

    Optional<InAppProduct> getInAppProductPort(String productId);

    List<InAppProduct> getInAppProductsPort(List<String> productIds);
}
