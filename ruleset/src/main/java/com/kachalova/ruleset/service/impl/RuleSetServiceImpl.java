package com.kachalova.ruleset.service.impl;

import com.kachalova.ruleset.dto.RuleSetRequestDto;
import com.kachalova.ruleset.dto.RuleSetResponseDto;
import com.kachalova.ruleset.entity.AnonymizationRuleEntity;
import com.kachalova.ruleset.entity.RuleSetEntity;
import com.kachalova.ruleset.mapper.RuleSetMapper;
import com.kachalova.ruleset.mapper.RuleSetMapper2;
import com.kachalova.ruleset.repository.RuleSetRepository;
import com.kachalova.ruleset.service.RuleSetService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RuleSetServiceImpl implements RuleSetService {

    private final RuleSetRepository repository;
    private final RuleSetMapper mapper;
    private final RuleSetMapper2 mapper2;

    @Override
    public RuleSetResponseDto createRuleSet(RuleSetRequestDto dto) {

        RuleSetEntity entity = mapper.toEntity(dto);
        // Устанавливаем обратную связь у правил
        if (entity.getRules() != null) {
            entity.getRules().forEach(rule -> rule.setRuleSet(entity));
        }
        RuleSetEntity saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    public RuleSetResponseDto updateRuleSet(UUID id, RuleSetRequestDto dto) {
        RuleSetEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RuleSet not found with id " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        existing.getRules().clear();

        if (dto.getRules() != null) {
            List<AnonymizationRuleEntity> rules = dto.getRules().stream()
                    .map(mapper::toEntity)
                    .toList();
            rules.forEach(rule -> rule.setRuleSet(existing));
            existing.getRules().addAll(rules);
        }

        RuleSetEntity updated = repository.save(existing);
        return mapper.toResponseDto(updated);
    }

    @Override
    public void deleteRuleSet(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public RuleSetResponseDto getRuleSetById(UUID id) {
        RuleSetEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RuleSet not found with id " + id));
        return mapper.toResponseDto(entity);
    }

    @Override
    public List<RuleSetResponseDto> getAllRuleSets() {
        return mapper.toResponseDtoList(repository.findAll());
    }
}
