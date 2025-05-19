package com.kachalova.fileprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.fileprocessing.dto.PersonalData;
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

    private static final String TOPIC = "raw-topic"; // замените на ваш актуальный топик

    public void processFile(MultipartFile file) {
        try (CSVParser parser = new CSVParser(
                new InputStreamReader(file.getInputStream()),
                CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<CSVRecord> records = parser.getRecords();

            for (CSVRecord record : records) {
                PersonalData dto = PersonalData.builder()
                        .birthDate(record.get("birthDate"))
                        .birthPlace(record.get("birthPlace"))
                        .passport(record.get("passport"))
                        .address(record.get("address"))
                        .phone(record.get("phone"))
                        .email(record.get("email"))
                        .inn(record.get("inn"))
                        .snils(record.get("snils"))
                        .card(record.get("card"))
                        .sourceTopic(TOPIC)
                        .build();

                String json = objectMapper.writeValueAsString(dto);
                kafkaTemplate.send(TOPIC, json);
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла: " + e.getMessage(), e);
        }
    }
}
