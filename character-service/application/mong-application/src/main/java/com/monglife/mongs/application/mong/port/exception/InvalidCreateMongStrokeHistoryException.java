package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateMongStrokeHistoryException extends ErrorException {

    public InvalidCreateMongStrokeHistoryException() {
        this.errorCode = ApplicationMongErrorCode.INVALID_CREATE_MONG_STROKE_HISTORY;
        this.result = Collections.emptyMap();
    }
}
