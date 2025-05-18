package com.kachalova.jobservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JobConsumer {

    @KafkaListener(topics = "jobs", groupId = "job-service-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        // Здесь можно обрабатывать полученные сообщения
    }
}
