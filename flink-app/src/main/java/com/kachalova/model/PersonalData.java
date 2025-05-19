package com.kachalova.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalData {
    private String userId;
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
