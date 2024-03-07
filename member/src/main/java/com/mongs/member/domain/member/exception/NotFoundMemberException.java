package com.mongs.member.domain.member.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NotFoundMemberException extends ErrorException {
    public NotFoundMemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundMemberException(Throwable e) {
        super(e);
    }

    public NotFoundMemberException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
