package com.kachalova.jobservice.controller;


import com.kachalova.jobservice.kafka.JobProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobProducer jobProducer;

    public JobController(JobProducer jobProducer) {
        this.jobProducer = jobProducer;
    }

    @PostMapping
    public ResponseEntity<String> sendJobMessage(@RequestBody String message) {
        jobProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to Kafka: " + message);
    }
}

