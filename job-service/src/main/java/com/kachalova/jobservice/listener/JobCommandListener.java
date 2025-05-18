package com.kachalova.jobservice.listener;

import com.kachalova.jobservice.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCommandListener {

    private final JobService jobService;

    @KafkaListener(topics = "job-commands", groupId = "job-service-group")
    public void listen(String message) {
        log.info("Received job command: {}", message);

        // Пример простого формата команды: "start:<jobId>" или "stop:<jobId>"
        if (message == null || !message.contains(":")) {
            log.warn("Invalid command format");
            return;
        }
        String[] parts = message.split(":");
        String command = parts[0];
        String jobIdStr = parts[1];

        try {
            var jobId = java.util.UUID.fromString(jobIdStr);
            switch (command.toLowerCase()) {
                case "start" -> jobService.startJob(jobId);
                case "stop" -> jobService.stopJob(jobId);
                default -> log.warn("Unknown command: {}", command);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID: {}", jobIdStr);
        }
    }
}
