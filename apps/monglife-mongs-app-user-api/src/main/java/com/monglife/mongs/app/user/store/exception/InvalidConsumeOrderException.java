package com.monglife.mongs.app.user.store.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.user.store.enums.StoreResponse;
import com.monglife.mongs.client.google.enums.GoogleResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidConsumeOrderException extends ErrorException {

    public InvalidConsumeOrderException(Long productOrderId, String purchaseToken) {
        this.response = StoreResponse.APP_USER_STORE_NOT_PURCHASE_ORDER;
        this.result = Map.of("productOrderId", productOrderId, "purchaseToken", purchaseToken);
    }
}
