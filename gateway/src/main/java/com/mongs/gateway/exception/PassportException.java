package com.mongs.gateway.exception;

public class PassportException extends RuntimeException {
    public PassportException(String message) {
        super(message);
    }
    public PassportException(Throwable e) {
        super(e);
    }
    public PassportException(String message, Throwable e) {
        super(message, e);
    }
}
