package com.kachalova.jobservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class JobRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String inputTopic;

    @NotBlank
    private String outputTopic;

    @NotNull
    private Long ruleSetId;

    private String schedule; // optional, cron expression
}
