package com.monglife.mongs.client.user.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.user.enums.UserResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateCollectionMongException extends ErrorException {

    public InvalidCreateCollectionMongException(String mongTypeCode) {
        this.response = UserResponse.CLIENT_USER_CREATE_COLLECTION_MONG;
        this.result = Collections.singletonMap("mongTypeCode", mongTypeCode);
    }
}
