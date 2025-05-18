package com.kachalova.jobservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class JobResponseDto {

    private UUID id;
    private String name;
    private String inputTopic;
    private String outputTopic;
    private Long ruleSetId;
    private String schedule;
    private String status;
}
