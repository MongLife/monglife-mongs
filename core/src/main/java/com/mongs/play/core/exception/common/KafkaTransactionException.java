package com.mongs.play.core.exception.common;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class KafkaTransactionException extends ErrorException {
    public KafkaTransactionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public KafkaTransactionException(Throwable e) {
        super(e);
    }

    public KafkaTransactionException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
