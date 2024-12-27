package com.monglife.mongs.client.user.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.user.enums.UserResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateCollectionMapException extends ErrorException {

    public InvalidCreateCollectionMapException(String mapTypeCode) {
        this.response = UserResponse.CLIENT_USER_CREATE_COLLECTION_MAP;
        this.result = Collections.singletonMap("mapTypeCode", mapTypeCode);
    }
}
