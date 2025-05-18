package com.kachalova.jobservice.service.impl;

import com.kachalova.jobservice.dto.JobRequestDto;
import com.kachalova.jobservice.dto.JobResponseDto;
import com.kachalova.jobservice.entity.JobEntity;
import com.kachalova.jobservice.entity.JobStatus;
import com.kachalova.jobservice.entity.RuleSetEntity;
import com.kachalova.jobservice.kafka.JobCommandProducer;
import com.kachalova.jobservice.mapper.JobMapper;
import com.kachalova.jobservice.repository.JobRepository;
import com.kachalova.jobservice.repository.RuleSetRepository;
import com.kachalova.jobservice.service.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final RuleSetRepository ruleSetRepository;  // Добавлено для доступа к RuleSetEntity
    private final JobMapper jobMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JobCommandProducer jobCommandProducer;

    private static final String START_JOB_TOPIC = "job-commands-start";
    private static final String STOP_JOB_TOPIC = "job-commands-stop";

    @Override
    public JobResponseDto createJob(JobRequestDto request) {
        RuleSetEntity ruleSetEntity = ruleSetRepository.findById(request.getRuleSet().getId())
                .orElseThrow(() -> new EntityNotFoundException("RuleSet not found"));

        JobEntity job = jobMapper.toEntity(request, ruleSetEntity);  // Передаем RuleSetEntity
        job.setStatus(JobStatus.CREATED);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.saveAndFlush(job);

        return jobMapper.toResponseDto(job);
    }

    @Override
    public List<JobResponseDto> getJobs(String status, int page, int size) {
        List<JobEntity> jobs;
        if (status != null) {
            JobStatus jobStatus = JobStatus.valueOf(status.toUpperCase());
            jobs = jobRepository.findByStatus(jobStatus, PageRequest.of(page, size));
        } else {
            jobs = jobRepository.findAll(PageRequest.of(page, size)).getContent();
        }
        return jobs.stream().map(jobMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public JobResponseDto getJobById(UUID jobId) {
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        return jobMapper.toResponseDto(job);
    }

    @Override
    public JobResponseDto updateJob(UUID jobId, JobRequestDto request) {
        JobEntity existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        RuleSetEntity ruleSetEntity = ruleSetRepository.findById(request.getRuleSet().getId())
                .orElseThrow(() -> new EntityNotFoundException("RuleSet not found"));

        jobMapper.updateEntityFromDto(request, existingJob);
        existingJob.setRuleSet(ruleSetEntity);  // Обновляем связанный RuleSetEntity
        existingJob.setUpdatedAt(LocalDateTime.now());

        jobRepository.saveAndFlush(existingJob);
        return jobMapper.toResponseDto(existingJob);
    }

    @Override
    public void deleteJob(UUID jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new EntityNotFoundException("Job not found");
        }
        jobRepository.deleteById(jobId);
    }

    @Override
    public void startJob(UUID jobId) {
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        job.setStatus(JobStatus.RUNNING);
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.saveAndFlush(job);

        // Отправляем команду в Kafka для запуска джоба
        jobCommandProducer.sendStartCommand(jobId.toString());
    }

    @Override
    public void stopJob(UUID jobId) {
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        job.setStatus(JobStatus.STOPPED);
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.saveAndFlush(job);

        // Отправляем команду в Kafka для остановки джоба
        jobCommandProducer.sendStopCommand(jobId.toString());
    }
}
