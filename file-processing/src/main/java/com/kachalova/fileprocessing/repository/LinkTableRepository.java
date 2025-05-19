package com.kachalova.fileprocessing.repository;


import com.kachalova.fileprocessing.entity.LinkTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LinkTableRepository extends JpaRepository<LinkTable, UUID> {
    Optional<LinkTable> findByAnonymizedData_Id(UUID anonymizedId);
    Optional<LinkTable> findById(UUID id);
}

