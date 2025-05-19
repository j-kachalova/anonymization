package com.kachalova.fileprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.fileprocessing.dto.PersonalDataDTO;
import com.kachalova.fileprocessing.entity.PersonalDataEntity;
import com.kachalova.fileprocessing.kafka.PersonalData;
import com.kachalova.fileprocessing.mapper.PersonalDataMapper;
import com.kachalova.fileprocessing.repository.PersonalDataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileProcessingService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final PersonalDataMapper personalDataMapper;
    private final PersonalDataRepository personalDataRepository;
    private static final String TOPIC = "raw-topic"; // замените на ваш актуальный топик

    public void processFile(MultipartFile file) {
        try (CSVParser parser = new CSVParser(
                new InputStreamReader(file.getInputStream()),
                CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<CSVRecord> records = parser.getRecords();

            for (CSVRecord record : records) {
                PersonalDataDTO dto = PersonalDataDTO.builder()
                        .birthDate(record.get("birthDate"))
                        .birthPlace(record.get("birthPlace"))
                        .passport(record.get("passport"))
                        .address(record.get("address"))
                        .phone(record.get("phone"))
                        .email(record.get("email"))
                        .inn(record.get("inn"))
                        .snils(record.get("snils"))
                        .card(record.get("card"))
                        .build();
                PersonalDataEntity entity = personalDataMapper.toEntity(dto);
                personalDataRepository.save(entity);
                PersonalData personalData = personalDataMapper.toKafkaModel(entity);
                personalData.setSourceTopic(TOPIC);
                String json = objectMapper.writeValueAsString(personalData);
                kafkaTemplate.send(TOPIC, json);
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла: " + e.getMessage(), e);
        }
    }

}
