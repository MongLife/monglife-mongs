package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidEvolutionException extends ErrorException {

    public InvalidEvolutionException() {
        this.errorCode = DomainMongErrorCode.INVALID_EVOLUTION;
        this.result = Collections.emptyMap();
    }
}
