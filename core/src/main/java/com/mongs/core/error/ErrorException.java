package com.mongs.core.error;

public class ErrorException extends RuntimeException {
    public ErrorException(String message) {
        super(message);
    }
    public ErrorException(Throwable e) {
        super(e);
    }
    public ErrorException(String message, Throwable e) {
        super(message, e);
    }
}
