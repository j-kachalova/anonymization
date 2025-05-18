package com.kachalova.jobservice.controller;

import com.kachalova.jobservice.dto.JobRequestDto;
import com.kachalova.jobservice.dto.JobResponseDto;
import com.kachalova.jobservice.kafka.JobProducer;
import com.kachalova.jobservice.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobProducer jobProducer;

    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobRequestDto jobRequest) {
        JobResponseDto createdJob = jobService.createJob(jobRequest);
        return ResponseEntity.ok(createdJob);
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDto>> getJobs(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<JobResponseDto> jobs = jobService.getJobs(status, page, size);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponseDto> getJobById(@PathVariable UUID jobId) {
        JobResponseDto job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponseDto> updateJob(@PathVariable UUID jobId,
                                                    @Valid @RequestBody JobRequestDto jobRequest) {
        JobResponseDto updatedJob = jobService.updateJob(jobId, jobRequest);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{jobId}/start")
    public ResponseEntity<String> startJob(@PathVariable UUID jobId) {
        jobService.startJob(jobId);
        return ResponseEntity.ok("Job started");
    }

    @PostMapping("/{jobId}/stop")
    public ResponseEntity<String> stopJob(@PathVariable UUID jobId) {
        jobService.stopJob(jobId);
        return ResponseEntity.ok("Job stopped");
    }

    @PostMapping("/test")
    public ResponseEntity<String> sendJobMessage(@RequestBody String message) {
        jobProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to Kafka: " + message);
    }
}
