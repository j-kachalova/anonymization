package com.kachalova.fileprocessing.mapper;

import com.kachalova.fileprocessing.dto.AnonymizedDataDto;
import com.kachalova.fileprocessing.entity.AnonymizedData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnonymizedDataMapper {
    AnonymizedDataDto toDto(AnonymizedData entity);
    AnonymizedData toEntity(AnonymizedDataDto dto);
}
