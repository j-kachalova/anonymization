package com.kachalova.jobservice.config;

import com.kachalova.jobservice.dto.JobResponseDto;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class JobResponseDtoSerializer implements Serializer<JobResponseDto> {

    @Override
    public byte[] serialize(String topic, JobResponseDto data) {
        // Преобразуем объект JobResponseDto в строку (например, JSON) и затем в байты
        if (data == null) {
            return null;
        }
        return data.toString().getBytes(StandardCharsets.UTF_8);  // Пример простого преобразования
    }
}
