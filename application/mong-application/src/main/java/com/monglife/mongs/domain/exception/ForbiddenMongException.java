package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class ForbiddenMongException extends ErrorException {

    public ForbiddenMongException() {
        this.errorCode = DomainMongErrorCode.FORBIDDEN_MONG;
        this.result = Collections.emptyMap();
    }
}
