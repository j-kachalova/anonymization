package com.kachalova.fileprocessing.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDataDTO {

    private String phone;
    private String email;
    private String birthDate;
    private String birthPlace;
    private String passport;
    private String address;
    private String inn;
    private String snils;
    private String card;
}
