package com.kachalova.fileprocessing.repository;


import com.kachalova.fileprocessing.entity.AnonymizedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnonymizedDataRepository extends JpaRepository<AnonymizedData, UUID> {
}
