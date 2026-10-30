package com.github.denegas.validator;

public interface Validator<T> {
    boolean isValid(T t);
}
