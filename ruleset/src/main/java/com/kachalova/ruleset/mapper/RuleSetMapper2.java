package com.kachalova.ruleset.mapper;

import com.kachalova.ruleset.dto.RuleSetRequestDto;
import com.kachalova.ruleset.entity.AnonymizationMethod;
import com.kachalova.ruleset.entity.AnonymizationRuleEntity;
import com.kachalova.ruleset.entity.RuleSetEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RuleSetMapper2 {

    public static RuleSetEntity toEntity(RuleSetRequestDto dto) {
        if (dto == null) {
            return null;
        }

        RuleSetEntity ruleSetEntity = RuleSetEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        if (dto.getRules() != null) {
            List<AnonymizationRuleEntity> rules = dto.getRules().stream()
                    .map(ruleDto -> {
                        AnonymizationRuleEntity ruleEntity = AnonymizationRuleEntity.builder()
                                .fieldName(ruleDto.getFieldName())
                                .method(AnonymizationMethod.fromString(ruleDto.getMethod()))
                                .parameters(ruleDto.getParameters())
                                .ruleSet(ruleSetEntity)  // Важно! Связываем правило с RuleSetEntity
                                .build();
                        return ruleEntity;
                    })
                    .collect(Collectors.toList());

            ruleSetEntity.setRules(rules);
        }

        return ruleSetEntity;
    }
}

