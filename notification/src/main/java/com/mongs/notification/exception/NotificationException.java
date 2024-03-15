package com.mongs.notification.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NotificationException extends ErrorException {
    public NotificationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotificationException(Throwable e) {
        super(e);
    }

    public NotificationException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
