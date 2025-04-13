package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughStarPointException extends ErrorException {

    public NotEnoughStarPointException() {
        this.errorCode = DomainMemberErrorCode.NOT_ENOUGH_STAR_POINT;
        this.result = Collections.emptyMap();
    }
}
