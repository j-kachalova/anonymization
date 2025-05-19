package com.kachalova.fileprocessing.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FileProcessingConsumer {
    @KafkaListener(topics = "your-kafka-topic", groupId = "file-group")
    public void listenStart(String message) {
        System.out.println("Received message: " + message);
        // Здесь можно обрабатывать полученные сообщения
    }
}
