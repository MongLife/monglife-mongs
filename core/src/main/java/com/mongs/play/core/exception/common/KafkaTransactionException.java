package com.mongs.play.core.exception.common;


import com.mongs.play.core.error.ErrorCode;

public class KafkaTransactionException extends CommonErrorException {
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
