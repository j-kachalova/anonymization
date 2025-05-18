package com.kachalova.ruleset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleSetResponseDto {
    private UUID id;
    private String name;
    private String description;
    private List<AnonymizationRuleDto> rules;
}
