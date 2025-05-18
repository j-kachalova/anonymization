package com.kachalova.fileprocessing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class FileProcessingService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public FileProcessingService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void processFile(MultipartFile file) throws IOException {
        // Преобразуем файл в строку или обработаем по частям
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Разбиение на отдельные сообщения
                kafkaTemplate.send("your-kafka-topic", line);
            }
        }
    }
}

