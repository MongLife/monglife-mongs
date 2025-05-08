package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyMaxSlotCountException extends ErrorException {

    public AlreadyMaxSlotCountException() {
        this.errorCode = DomainMemberErrorCode.ALREADY_MAX_SLOT_COUNT;
        this.result = Collections.emptyMap();
    }
}
