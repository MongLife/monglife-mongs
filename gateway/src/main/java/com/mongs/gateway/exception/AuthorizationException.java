package com.mongs.gateway.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
    public AuthorizationException(Throwable e) {
        super(e);
    }
    public AuthorizationException(String message, Throwable e) {
        super(message, e);
    }
}
