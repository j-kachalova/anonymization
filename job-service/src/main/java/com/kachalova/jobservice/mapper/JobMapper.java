package com.kachalova.jobservice.mapper;

import com.kachalova.jobservice.dto.JobRequestDto;
import com.kachalova.jobservice.dto.JobResponseDto;
import com.kachalova.jobservice.entity.JobEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobEntity toEntity(JobRequestDto dto);

    JobResponseDto toDto(JobEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(JobRequestDto dto, @MappingTarget JobEntity entity);
}
