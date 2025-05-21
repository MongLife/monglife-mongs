package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidUseInventoryItemException extends ErrorException {

    public InvalidUseInventoryItemException() {
        this.errorCode = ApplicationMongErrorCode.INVALID_USE_INVENTORY_ITEM;
        this.result = Collections.emptyMap();
    }
}
