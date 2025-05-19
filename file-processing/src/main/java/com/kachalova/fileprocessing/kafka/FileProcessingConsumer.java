package com.kachalova.fileprocessing.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.fileprocessing.entity.AnonymizedData;
import com.kachalova.fileprocessing.entity.LinkTable;
import com.kachalova.fileprocessing.kafka.KafkaData;
import com.kachalova.fileprocessing.repository.AnonymizedDataRepository;
import com.kachalova.fileprocessing.repository.LinkTableRepository;
import com.kachalova.fileprocessing.repository.PersonalDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileProcessingConsumer {
    private final ObjectMapper objectMapper;
    private final AnonymizedDataRepository anonymizedDataRepository;
    private final LinkTableRepository linkTableRepository;
    private final PersonalDataRepository personalDataRepository;
    @KafkaListener(topics = "kafka-file-output", groupId = "file-group")
    public void listenStart(String message) throws JsonProcessingException {
        System.out.println("Received message: " + message);
        try {
            KafkaData kafkaData = objectMapper.readValue(message, KafkaData.class);

            // Сохраняем обезличенные данные
            AnonymizedData anonymized = AnonymizedData.builder()
                    .email(kafkaData.getEmail())
                    .phone(kafkaData.getPhone())
                    .passport(kafkaData.getPassport())
                    .card(kafkaData.getCard())
                    .inn(kafkaData.getInn())
                    .snils(kafkaData.getSnils())
                    .birthPlace(kafkaData.getBirthPlace())
                    .birthDate(kafkaData.getBirthDate())
                    .address(kafkaData.getAddress())
                    .build();

            anonymized = anonymizedDataRepository.save(anonymized);

            // Сохраняем связь
            LinkTable link = new LinkTable();
            link.setPersonalData(personalDataRepository.findById(kafkaData.getId()).get());
            link.setAnonymizedData(anonymized);

            linkTableRepository.save(link);

            System.out.println("✅ Данные сохранены: " + kafkaData+link);

        } catch (Exception e) {
            System.out.println("ОШИБКА");
            e.printStackTrace();
        }
        // Здесь можно обрабатывать полученные сообщения
    }
}
