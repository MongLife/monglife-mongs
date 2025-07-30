package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.domain.member.model.InAppOrder;
import com.monglife.mongs.domain.member.model.InAppProduct;

import java.util.List;
import java.util.Optional;

public interface GooglePaymentPort {

    Optional<InAppOrder> getInAppOrderPort(String productId, String socialOrderId, String purchaseToken);

    Optional<InAppOrder> consumeInAppOrderPort(InAppOrder inAppOrder);

    Optional<InAppProduct> getInAppProductPort(String productId);

    List<InAppProduct> getInAppProductsPort();
}
