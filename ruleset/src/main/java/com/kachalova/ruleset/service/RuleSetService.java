package com.kachalova.ruleset.service;

import com.kachalova.ruleset.dto.RuleSetRequestDto;
import com.kachalova.ruleset.dto.RuleSetResponseDto;

import java.util.List;
import java.util.UUID;

public interface RuleSetService {
    RuleSetResponseDto createRuleSet(RuleSetRequestDto dto);

    RuleSetResponseDto updateRuleSet(UUID id, RuleSetRequestDto dto);

    void deleteRuleSet(UUID id);

    RuleSetResponseDto getRuleSetById(UUID id);

    List<RuleSetResponseDto> getAllRuleSets();
}
