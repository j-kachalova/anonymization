package com.kachalova.fileprocessing.repository;

import com.kachalova.fileprocessing.entity.PersonalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonalDataRepository extends JpaRepository<PersonalDataEntity, UUID> {
    Optional<PersonalDataEntity> findByEmail(String email);

    Optional<PersonalDataEntity> findByPhone(String phone);

    Optional<PersonalDataEntity> findByPassport(String passport);
}