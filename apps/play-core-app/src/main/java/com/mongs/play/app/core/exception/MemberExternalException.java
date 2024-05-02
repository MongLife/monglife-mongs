package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

public class MemberExternalException extends ErrorException {
    public MemberExternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberExternalException(Throwable e) {
        super(e);
    }

    public MemberExternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
