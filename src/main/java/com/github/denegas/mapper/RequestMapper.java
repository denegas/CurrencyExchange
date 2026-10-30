package com.github.denegas.mapper;

public interface RequestMapper<E, Req> {
    E toEntity(Req dto);
}
