package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidMongStateException extends ErrorException {

    public InvalidMongStateException() {
        this.errorCode = DomainMongErrorCode.INVALID_MONG_STATE;
        this.result = Collections.emptyMap();
    }
}
