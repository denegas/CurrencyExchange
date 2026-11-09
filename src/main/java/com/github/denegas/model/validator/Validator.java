package com.github.denegas.model.validator;

public interface Validator<T> {
    void validate(T t);
}
