package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsRandomDrawItemsException extends ErrorException {

    public NotExistsRandomDrawItemsException() {
        this.errorCode = ApplicationMongErrorCode.NOT_EXISTS_RANDOM_DRAW_ITEMS;
        this.result = Collections.emptyMap();
    }
}
