package com.example.util;

public class ThrowException extends RuntimeException {

    private Exception originalException;

    public ThrowException(Exception originalException) {
        this.originalException = originalException;
    }

    public Exception getOriginalException() {
        return originalException;
    }

    public void setOriginalException(Exception originalException) {
        this.originalException = originalException;
    }
}
