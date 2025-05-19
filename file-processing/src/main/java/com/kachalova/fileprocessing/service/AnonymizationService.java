package com.kachalova.fileprocessing.service;


import com.kachalova.fileprocessing.dto.AnonymizedDataDto;
import com.kachalova.fileprocessing.dto.PersonalDataDTO;
import com.kachalova.fileprocessing.entity.AnonymizedData;
import com.kachalova.fileprocessing.entity.LinkTable;
import com.kachalova.fileprocessing.entity.PersonalDataEntity;
import com.kachalova.fileprocessing.mapper.AnonymizedDataMapper;
import com.kachalova.fileprocessing.mapper.PersonalDataMapper;
import com.kachalova.fileprocessing.repository.AnonymizedDataRepository;
import com.kachalova.fileprocessing.repository.LinkTableRepository;
import com.kachalova.fileprocessing.repository.PersonalDataRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnonymizationService {

    private final PersonalDataRepository originalRepo;
    private final PersonalDataMapper originalDataMapper;
    private final AnonymizedDataMapper anonymizedDataMapper;
    private final AnonymizedDataRepository anonymizedRepo;
    private final LinkTableRepository linkRepo;


    public PersonalDataEntity findOriginalById(UUID id) {
        return originalRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Данные не найдены: " + id));
    }

    public Optional<AnonymizedDataDto> getAnonymizedDto(UUID anonymizedId) {
        return anonymizedRepo.findById(anonymizedId)
                .map(anonymizedDataMapper::toDto);
    }
    public Optional<PersonalDataDTO> getOriginalDtoByAnonymizedId(UUID anonymizedId) {
        return linkRepo.findByAnonymizedData_Id(anonymizedId)
                .flatMap(link -> originalRepo.findById(link.getPersonalData().getId()))
                .map(originalDataMapper::toDto);
    }
    public List<PersonalDataEntity> getAllPersonalData() {
        List<PersonalDataEntity> data = originalRepo.findAll();
        return data;
    }
    public List<LinkTable> getAllLinks() {
        return linkRepo.findAll();
    }
    public List<AnonymizedData> getAllAnonymizedData() {
        return anonymizedRepo.findAll();
    }
    public LinkTable getLinkById(UUID id) {
        return linkRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("LinkTable с id " + id + " не найден"));
    }
}
