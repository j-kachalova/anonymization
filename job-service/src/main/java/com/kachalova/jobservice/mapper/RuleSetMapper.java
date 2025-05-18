package com.kachalova.jobservice.mapper;

import com.kachalova.jobservice.dto.AnonymizationRuleDto;
import com.kachalova.jobservice.dto.RuleSetRequestDto;
import com.kachalova.jobservice.dto.RuleSetResponseDto;
import com.kachalova.jobservice.entity.RuleSetEntity;
import com.kachalova.jobservice.entity.AnonymizationRuleEntity;

import java.util.List;
import java.util.stream.Collectors;

public class RuleSetMapper {

    public static RuleSetEntity toEntity(RuleSetRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        List<AnonymizationRuleEntity> rules = requestDto.getRules().stream()
                .map(ruleDto -> AnonymizationRuleMapper.toEntity(ruleDto, null)) // В данном случае ruleSet передается как null
                .collect(Collectors.toList());

        return RuleSetEntity.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .rules(rules)
                .build();
    }

    public static RuleSetResponseDto toResponseDto(RuleSetEntity entity) {
        if (entity == null) {
            return null;
        }

        List<AnonymizationRuleDto> rules = entity.getRules().stream()
                .map(AnonymizationRuleMapper::toDto)
                .collect(Collectors.toList());

        return RuleSetResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .rules(rules)
                .build();
    }
}
