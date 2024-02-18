package com.mongs.gateway.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
    public TokenNotFoundException(Throwable e) {
        super(e);
    }
    public TokenNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
