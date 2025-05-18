package com.kachalova.jobservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class JobResponseDto {

    private UUID id;
    private String name;
    private String inputTopic;
    private String outputTopic;
    private RuleSetResponseDto ruleSet;
    private String schedule;
    private String status;
}
