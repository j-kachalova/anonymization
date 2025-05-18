package com.kachalova.jobservice.mapper;

import com.kachalova.jobservice.dto.JobRequestDto;
import com.kachalova.jobservice.dto.JobResponseDto;
import com.kachalova.jobservice.entity.JobEntity;
import com.kachalova.jobservice.entity.JobStatus;
import com.kachalova.jobservice.entity.RuleSetEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class JobMapper {

    public static JobEntity toEntity(JobRequestDto requestDto, RuleSetEntity ruleSetEntity) {
        if (requestDto == null) {
            return null;
        }

        return JobEntity.builder()
                .name(requestDto.getName())
                .inputTopic(requestDto.getInputTopic())
                .outputTopic(requestDto.getOutputTopic())
                .ruleSet(ruleSetEntity)  // Устанавливаем связанную сущность RuleSetEntity
                .schedule(requestDto.getSchedule())
               // Начальный статус, можно изменить в процессе работы
                .createdAt(LocalDateTime.now()) // Устанавливаем время создания
                .updatedAt(LocalDateTime.now()) // Устанавливаем время обновления
                .build();
    }

    public JobResponseDto toResponseDto(JobEntity entity) {
        if (entity == null) {
            return null;
        }

        return JobResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .inputTopic(entity.getInputTopic())
                .outputTopic(entity.getOutputTopic())
                .ruleSet(RuleSetMapper.toResponseDto(entity.getRuleSet()))  // Используем RuleSetMapper
                .schedule(entity.getSchedule())
                .status(entity.getStatus().name())  // Преобразуем статус в строку
                .build();
    }
    public static void updateEntityFromDto(JobRequestDto dto, JobEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        // Обновляем поля сущности на основе данных из DTO
        entity.setName(dto.getName());
        entity.setInputTopic(dto.getInputTopic());
        entity.setOutputTopic(dto.getOutputTopic());
        entity.setSchedule(dto.getSchedule());
        // Не изменяем статус, так как это будет сделано отдельно (например, в сервисе).
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
