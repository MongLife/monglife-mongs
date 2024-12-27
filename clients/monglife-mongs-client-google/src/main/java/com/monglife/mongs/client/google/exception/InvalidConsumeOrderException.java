package com.monglife.mongs.client.google.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.google.enums.GoogleResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidConsumeOrderException extends ErrorException {

    public InvalidConsumeOrderException(Long productOrderId, String purchaseToken) {
        this.response = GoogleResponse.CLIENT_GOOGLE_CONSUME_ORDER_FAIL;
        this.result = Map.of("productOrderId", productOrderId, "purchaseToken", purchaseToken);
    }
}
