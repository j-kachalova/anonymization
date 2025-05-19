package com.kachalova.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymizationRuleDto {
    private UUID id;
    private String fieldName;
    private String method;
    private String parameters;
}