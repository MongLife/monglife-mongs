package com.monglife.mongs.client.user.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.user.enums.UserResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidGetCollectionMongException extends ErrorException {

    public InvalidGetCollectionMongException() {
        this.response = UserResponse.CLIENT_USER_GET_COLLECTION_MONG;
        this.result = Collections.emptyMap();
    }
}
