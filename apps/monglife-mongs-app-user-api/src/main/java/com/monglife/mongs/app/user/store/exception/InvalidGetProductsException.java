package com.monglife.mongs.app.user.store.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.user.store.enums.StoreResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidGetProductsException extends ErrorException {

    public InvalidGetProductsException() {
        this.response = StoreResponse.USER_STORE_GET_PRODUCTS_FAIL;
        this.result = Collections.emptyMap();
    }
}
