package com.mongs.member.domain.member.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class InvalidModifySlotCountException extends ErrorException {
    public InvalidModifySlotCountException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidModifySlotCountException(Throwable e) {
        super(e);
    }

    public InvalidModifySlotCountException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
