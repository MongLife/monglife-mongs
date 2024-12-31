package com.monglife.mongs.app.user.store.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.user.store.enums.StoreResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class AlreadyConsumeOrderException extends ErrorException {

    public AlreadyConsumeOrderException(Long productOrderId, String purchaseToken) {
        this.response = StoreResponse.APP_USER_STORE_ALREADY_CONSUME_ORDER;
        this.result = Map.of("productOrderId", productOrderId, "purchaseToken", purchaseToken);
    }
}
