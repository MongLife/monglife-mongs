package com.mongs.auth.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(Throwable e) {
        super(e);
    }
    public NotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
