package com.monglife.mongs.client.google.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.google.enums.GoogleResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidGetOrderException extends ErrorException {

    public InvalidGetOrderException(String productId) {
        this.response = GoogleResponse.CLIENT_GOOGLE_GET_ORDER_FAIL;
        this.result = Map.of("productId", productId);
    }
}
