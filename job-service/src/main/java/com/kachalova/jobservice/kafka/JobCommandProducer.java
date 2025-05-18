package com.kachalova.jobservice.kafka;

import com.kachalova.jobservice.dto.JobResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobCommandProducer {

    private final KafkaTemplate<String, JobResponseDto> kafkaTemplate;  // Изменено на DTO
    private static final String START_JOB_TOPIC = "job-commands-start";
    private static final String STOP_JOB_TOPIC = "job-commands-stop";

    // Отправляем команду старта с полным объектом JobResponseDto
    public void sendStartCommand(JobResponseDto jobResponseDto) {
        kafkaTemplate.send(START_JOB_TOPIC, jobResponseDto);
        log.info("Sent start command for jobId {}", jobResponseDto.getId());
    }

    // Отправляем команду остановки с полным объектом JobResponseDto
    public void sendStopCommand(JobResponseDto jobResponseDto) {
        kafkaTemplate.send(STOP_JOB_TOPIC, jobResponseDto);
        log.info("Sent stop command for jobId {}", jobResponseDto.getId());
    }
}
