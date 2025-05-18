package com.kachalova.ruleset.repository;

import com.kachalova.ruleset.entity.RuleSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuleSetRepository extends JpaRepository<RuleSetEntity, UUID> {
}

