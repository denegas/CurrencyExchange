package com.github.denegas.exception;

public class NothingThereException extends RuntimeException {
    public NothingThereException(String message) {
        super(message);
    }

    public NothingThereException(String message, Throwable e) {
        super(message, e);
    }
}
