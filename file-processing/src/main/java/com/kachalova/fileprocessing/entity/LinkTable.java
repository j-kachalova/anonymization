package com.kachalova.fileprocessing.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class LinkTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "personalData_id")
    private PersonalDataEntity personalData;
    @OneToOne
    @JoinColumn(name = "anonymized_id")
    private AnonymizedData anonymizedData;


}

