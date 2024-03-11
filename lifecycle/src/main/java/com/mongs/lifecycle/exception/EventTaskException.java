package com.mongs.lifecycle.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class EventTaskException extends ErrorException {
    public EventTaskException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EventTaskException(Throwable e) {
        super(e);
    }

    public EventTaskException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
