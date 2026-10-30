package com.github.denegas.mapper;

public interface ResponseMapper<E, Resp> {
    Resp toDto(E entity);
}
