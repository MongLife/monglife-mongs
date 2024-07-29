package com.mongs.play.core.exception.module;

import com.mongs.play.core.error.ErrorCode;

public class NotFoundException extends ModuleErrorException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(Throwable e) {
        super(e);
    }

    public NotFoundException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
