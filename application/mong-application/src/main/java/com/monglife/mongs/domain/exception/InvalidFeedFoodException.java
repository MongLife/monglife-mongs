package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidFeedFoodException extends ErrorException {

    public InvalidFeedFoodException() {
        this.errorCode = DomainMongErrorCode.INVALID_FEED_FOOD;
        this.result = Collections.emptyMap();
    }
}
