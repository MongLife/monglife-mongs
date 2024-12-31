package com.monglife.mongs.client.google.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.google.enums.GoogleResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class GetOrderException extends ErrorException {

    public GetOrderException(String productId, String orderId, String purchaseToken) {
        this.response = GoogleResponse.CLIENT_GOOGLE_GET_ORDER_FAIL;
        this.result = Map.of("productId", productId, "orderId", orderId, "purchaseToken", purchaseToken);
    }
}
