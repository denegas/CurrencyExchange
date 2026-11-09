package com.github.denegas.model.mapper;

public interface ResponseMapper<E, Resp> {
    Resp toDto(E entity);
}
