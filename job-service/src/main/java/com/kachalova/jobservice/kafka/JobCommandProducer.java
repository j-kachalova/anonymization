package com.kachalova.jobservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.jobservice.dto.JobResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobCommandProducer {

    private final KafkaTemplate<String, String> kafkaTemplate; // <- теперь тип String
    private final ObjectMapper objectMapper;
    private final TopicService topicService;
    private static final String START_JOB_TOPIC = "job-commands-start";
    private static final String STOP_JOB_TOPIC = "job-commands-stop";

    public void sendStartCommand(JobResponseDto jobResponseDto) {
        topicService.createTopic(jobResponseDto.getInputTopic(), 1, (short) 1);
        try {
            String json = objectMapper.writeValueAsString(jobResponseDto); // ✅ сериализация
            kafkaTemplate.send(START_JOB_TOPIC, json);
            log.info("✅ Sent START command as JSON for jobId {}: {}", jobResponseDto.getId(), json);
        } catch (JsonProcessingException e) {
            log.error("❌ Ошибка сериализации при отправке старта джобы", e);
        }
    }

    public void sendStopCommand(JobResponseDto jobResponseDto) {
        try {
            String json = objectMapper.writeValueAsString(jobResponseDto);
            kafkaTemplate.send(STOP_JOB_TOPIC, json);
            log.info("✅ Sent STOP command as JSON for jobId {}: {}", jobResponseDto.getId(), json);
        } catch (JsonProcessingException e) {
            log.error("❌ Ошибка сериализации при отправке остановки джобы", e);
        }
    }
}
