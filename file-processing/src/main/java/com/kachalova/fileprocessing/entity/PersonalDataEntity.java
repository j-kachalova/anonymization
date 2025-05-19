package com.kachalova.fileprocessing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "personal_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDataEntity {

    @Id
    @GeneratedValue
    private UUID userId;

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
