package com.github.denegas.exception;

public class InvalidExchangeRateException extends RuntimeException {
    public InvalidExchangeRateException(String message) {
        super(message);
    }

    public InvalidExchangeRateException(String message, Throwable e) {
        super(message, e);
    }
}
