package com.meli.msfuegoquasar.exceptions;


public class FallbackMethodException extends RuntimeException {

    public FallbackMethodException(String message) {
        super(message);
    }

    public FallbackMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public FallbackMethodException(Throwable cause) {
        super(cause);
    }

}

