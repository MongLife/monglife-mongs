package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

public class MemberInternalException extends ErrorException {
    public MemberInternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberInternalException(Throwable e) {
        super(e);
    }

    public MemberInternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
