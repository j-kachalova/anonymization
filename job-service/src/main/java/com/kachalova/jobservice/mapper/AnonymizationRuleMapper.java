package com.kachalova.jobservice.mapper;

import com.kachalova.jobservice.dto.AnonymizationRuleDto;
import com.kachalova.jobservice.entity.AnonymizationMethod;
import com.kachalova.jobservice.entity.AnonymizationRuleEntity;
import com.kachalova.jobservice.entity.RuleSetEntity;


public class AnonymizationRuleMapper {

    public static AnonymizationRuleEntity toEntity(AnonymizationRuleDto dto, RuleSetEntity ruleSet) {
        if (dto == null) {
            return null;
        }

        return AnonymizationRuleEntity.builder()
                .id(dto.getId())
                .fieldName(dto.getFieldName())
                .method(AnonymizationMethod.fromString(dto.getMethod())) // Преобразуем строку в enum
                .parameters(dto.getParameters())
                .ruleSet(ruleSet) // Принимаем объект RuleSetEntity
                .build();
    }

    public static AnonymizationRuleDto toDto(AnonymizationRuleEntity entity) {
        if (entity == null) {
            return null;
        }

        return AnonymizationRuleDto.builder()
                .id(entity.getId())
                .fieldName(entity.getFieldName())
                .method(entity.getMethod().name()) // Преобразуем enum в строку
                .parameters(entity.getParameters())
                .build();
    }
}
