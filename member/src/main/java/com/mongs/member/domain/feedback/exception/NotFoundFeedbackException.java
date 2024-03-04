package com.mongs.member.domain.feedback.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NotFoundFeedbackException extends ErrorException {
    public NotFoundFeedbackException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundFeedbackException(Throwable e) {
        super(e);
    }

    public NotFoundFeedbackException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
