package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class ForbiddenInventoryItemException extends ErrorException {

    public ForbiddenInventoryItemException() {
        this.errorCode = DomainMongErrorCode.FORBIDDEN_INVENTORY_ITEM;
        this.result = Collections.emptyMap();
    }
}
