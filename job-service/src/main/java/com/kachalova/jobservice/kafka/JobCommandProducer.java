package com.kachalova.jobservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobCommandProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "job-commands";

    public void sendStartCommand(String jobId) {
        String command = "start:" + jobId;
        kafkaTemplate.send(topic, command);
        log.info("Sent start command for jobId {}", jobId);
    }

    public void sendStopCommand(String jobId) {
        String command = "stop:" + jobId;
        kafkaTemplate.send(topic, command);
        log.info("Sent stop command for jobId {}", jobId);
    }
}
