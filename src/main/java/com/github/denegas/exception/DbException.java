package com.github.denegas.exception;

public class DbException extends RuntimeException {
    public DbException(String message) {
        super(message);
    }
    public DbException(Throwable e) {
        super(e);
    }
    public DbException(String message, Throwable e) {
        super(message, e);
    }

}
