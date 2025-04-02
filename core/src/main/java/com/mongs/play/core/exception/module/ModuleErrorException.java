package com.mongs.play.core.exception.module;

import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class ModuleErrorException extends ErrorException {
    public ModuleErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ModuleErrorException(Throwable e) {
        super(e);
    }

    public ModuleErrorException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
