package com.kachalova.ruleset.controller;

import com.kachalova.ruleset.dto.RuleSetRequestDto;
import com.kachalova.ruleset.dto.RuleSetResponseDto;
import com.kachalova.ruleset.service.RuleSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rulesets")
@RequiredArgsConstructor
public class RuleSetController {

    private final RuleSetService ruleSetService;

    @PostMapping
    public ResponseEntity<RuleSetResponseDto> createRuleSet(@Valid @RequestBody RuleSetRequestDto requestDto) {
        return ResponseEntity.ok(ruleSetService.createRuleSet(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<RuleSetResponseDto>> getAllRuleSets() {
        return ResponseEntity.ok(ruleSetService.getAllRuleSets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RuleSetResponseDto> getRuleSetById(@PathVariable UUID id) {
        return ResponseEntity.ok(ruleSetService.getRuleSetById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuleSetResponseDto> updateRuleSet(
            @PathVariable UUID id,
            @Valid @RequestBody RuleSetRequestDto requestDto
    ) {
        return ResponseEntity.ok(ruleSetService.updateRuleSet(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRuleSet(@PathVariable UUID id) {
        ruleSetService.deleteRuleSet(id);
        return ResponseEntity.noContent().build();
    }
}
