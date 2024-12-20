package com.monglife.mongs.app.user.store.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.user.store.enums.StoreResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidConsumeOrderException extends ErrorException {

    public InvalidConsumeOrderException(String productId, String purchaseToken) {
        this.response = StoreResponse.USER_STORE_INVALID_PURCHASE_VERIFY;
        this.result = Map.of("productId", productId, "purchaseToken", purchaseToken);
    }
}
