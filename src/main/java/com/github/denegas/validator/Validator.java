package com.github.denegas.validator;

public interface Validator<T> {
    void validate(T t);
}
