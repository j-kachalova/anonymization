package com.kachalova.fileprocessing.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaData {
    private UUID id;
    private String phone;
    private String email;
    private String birthDate;
    private String birthPlace;
    private String passport;
    private String address;
    private String inn;
    private String snils;
    private String card;
    private String sourceTopic;
}
