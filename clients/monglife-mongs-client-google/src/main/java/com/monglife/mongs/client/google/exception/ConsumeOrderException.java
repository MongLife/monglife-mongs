package com.monglife.mongs.client.google.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.google.enums.GoogleResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class ConsumeOrderException extends ErrorException {

    public ConsumeOrderException(String productId, String purchaseToken) {
        this.response = GoogleResponse.CLIENT_GOOGLE_CONSUME_ORDER;
        this.result = Map.of("productId", productId, "purchaseToken", purchaseToken);
    }
}
