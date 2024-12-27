package com.monglife.mongs.client.google.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.google.enums.GoogleResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidGetProductsException extends ErrorException {

    public InvalidGetProductsException() {
        this.response = GoogleResponse.CLIENT_GOOGLE_GET_PRODUCTS_FAIL;
        this.result = Collections.emptyMap();
    }
}
