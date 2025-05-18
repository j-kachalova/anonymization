package com.kachalova.jobservice.service;

import com.kachalova.jobservice.dto.JobRequestDto;
import com.kachalova.jobservice.dto.JobResponseDto;

import java.util.List;
import java.util.UUID;

public interface JobService {
    JobResponseDto createJob(JobRequestDto request);
    List<JobResponseDto> getJobs(String status, int page, int size);
    JobResponseDto getJobById(UUID jobId);
    JobResponseDto updateJob(UUID jobId, JobRequestDto request);
    void deleteJob(UUID jobId);
    void startJob(UUID jobId);
    void stopJob(UUID jobId);
}
