package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidFeedSnackException extends ErrorException {

    public InvalidFeedSnackException() {
        this.errorCode = DomainMongErrorCode.INVALID_FEED_SNACK;
        this.result = Collections.emptyMap();
    }
}
