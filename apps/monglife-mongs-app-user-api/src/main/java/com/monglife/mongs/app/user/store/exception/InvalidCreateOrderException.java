package com.monglife.mongs.app.user.store.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import com.monglife.mongs.app.user.store.enums.StoreResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidCreateOrderException extends ErrorException {

    public InvalidCreateOrderException(String productId) {
        this.response = StoreResponse.USER_STORE_CREATE_ORDER_FAIL;
        this.result = Map.of("productId", productId);
    }
}
