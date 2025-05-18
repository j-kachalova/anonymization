package com.kachalova.jobservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JobConsumer {

    @KafkaListener(topics = "job-commands-start", groupId = "job-service-group")
    public void listenStart(String message) {
        System.out.println("Received message: " + message);
        // Здесь можно обрабатывать полученные сообщения
    }
    @KafkaListener(topics = "job-commands-stop", groupId = "job-service-group")
    public void listenStop(String message) {
        System.out.println("Received message: " + message);
        // Здесь можно обрабатывать полученные сообщения
    }
}
