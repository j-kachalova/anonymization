package com.kachalova.jobservice.repository;

import com.kachalova.jobservice.entity.JobEntity;
import com.kachalova.jobservice.entity.JobStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
    List<JobEntity> findByStatus(JobStatus status, Pageable pageable);
}
