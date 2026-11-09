package com.github.denegas.model.mapper;

public interface RequestMapper<E, Req> {
    E toEntity(Req dto);
}
