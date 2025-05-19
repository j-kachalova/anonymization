package com.kachalova.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDto {
    private UUID id;
    private String name;
    private String inputTopic;
    private String outputTopic;
    private RuleSetResponseDto ruleSet;
    private String schedule;
    private String status;
}
