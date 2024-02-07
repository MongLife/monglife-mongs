package com.mongs.auth.exception;

public class ParameterValidateException extends RuntimeException {
    public ParameterValidateException(String message) {
        super(message);
    }
    public ParameterValidateException(Throwable e) {
        super(e);
    }
    public ParameterValidateException(String message, Throwable e) {
        super(message, e);
    }
}
