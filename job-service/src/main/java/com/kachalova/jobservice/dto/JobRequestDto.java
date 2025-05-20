package com.kachalova.jobservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class JobRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String inputTopic;


    private String outputTopic;

    @NotNull
    private RuleSetResponseDto ruleSet;

    private String schedule; // optional, cron expression
}
