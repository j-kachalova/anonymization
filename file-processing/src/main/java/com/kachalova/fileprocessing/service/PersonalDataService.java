package com.kachalova.fileprocessing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.fileprocessing.dto.PersonalDataDTO;
import com.kachalova.fileprocessing.entity.PersonalDataEntity;
import com.kachalova.fileprocessing.kafka.PersonalData;
import com.kachalova.fileprocessing.mapper.PersonalDataMapper;
import com.kachalova.fileprocessing.repository.PersonalDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalDataService {

    private final PersonalDataRepository repository;
    private final PersonalDataMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void process(PersonalDataDTO dto) throws JsonProcessingException {
        PersonalDataEntity saved = repository.save(mapper.toEntity(dto));
        PersonalData kafkaModel = mapper.toKafkaModel(saved);
        String json = objectMapper.writeValueAsString(kafkaModel);
        kafkaTemplate.send("raw-topic", json);
    }
}
