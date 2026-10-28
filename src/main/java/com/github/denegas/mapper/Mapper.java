package com.github.denegas.mapper;

public interface Mapper<ReqDto, RespDto, Entity> {
    RespDto toDto(Entity entity);

    Entity toEntity(ReqDto dto);


}
