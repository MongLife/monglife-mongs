package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateMongFeedHistoryException extends ErrorException {

    public InvalidCreateMongFeedHistoryException() {
        this.errorCode = ApplicationMongErrorCode.INVALID_CREATE_MONG_FEED_HISTORY;
        this.result = Collections.emptyMap();
    }
}
