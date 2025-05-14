package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class ForbiddenInventoryItemException extends ErrorException {

    public ForbiddenInventoryItemException() {
        this.errorCode = DomainMongErrorCode.FORBIDDEN_INVENTORY_ITEM;
        this.result = Collections.emptyMap();
    }
}
