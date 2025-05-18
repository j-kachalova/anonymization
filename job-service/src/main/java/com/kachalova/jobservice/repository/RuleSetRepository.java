package com.kachalova.jobservice.repository;

import com.kachalova.jobservice.entity.RuleSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuleSetRepository extends JpaRepository<RuleSetEntity, UUID> {
}

