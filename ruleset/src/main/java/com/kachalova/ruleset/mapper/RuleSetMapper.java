package com.kachalova.ruleset.mapper;

import com.kachalova.ruleset.dto.*;
import com.kachalova.ruleset.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RuleSetMapper {

    RuleSetEntity toEntity(RuleSetRequestDto dto);

    AnonymizationRuleEntity toEntity(AnonymizationRuleDto dto);

    AnonymizationRuleDto toDto(AnonymizationRuleEntity entity);

    List<RuleSetResponseDto> toResponseDtoList(List<RuleSetEntity> entities);

    RuleSetResponseDto toResponseDto(RuleSetEntity entity);
}

