package com.kachalova.fileprocessing.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kachalova.fileprocessing.dto.AnonymizedDataDto;
import com.kachalova.fileprocessing.dto.PersonalDataDTO;
import com.kachalova.fileprocessing.entity.AnonymizedData;
import com.kachalova.fileprocessing.entity.LinkTable;
import com.kachalova.fileprocessing.entity.PersonalDataEntity;
import com.kachalova.fileprocessing.kafka.FileProcessingService;
import com.kachalova.fileprocessing.mapper.PersonalDataMapper;
import com.kachalova.fileprocessing.service.AnonymizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/anonymization")
@RequiredArgsConstructor
public class AnonymizationController {

    private final FileProcessingService fileProcessingService;
    private final AnonymizationService anonymizationService;
    private final PersonalDataMapper originalDataMapper;

    @PostMapping("/anonymizeData")
    public ResponseEntity<?> anonymizeData(@RequestBody PersonalDataDTO dto, @RequestParam("inputTopic") String inputTopic) throws JsonProcessingException {
        try {
        fileProcessingService.process(dto, inputTopic);
        return ResponseEntity.ok("Данные отправлены в Kafka для обезличивания в топик " + inputTopic);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке данных: " + e.getMessage());
        }
    }


    // 3. Получить обезличенные данные по анонимному ID
    @GetMapping("/anonymized/{anonymizedId}")
    public ResponseEntity<AnonymizedDataDto> getAnonymized(@PathVariable UUID anonymizedId) {
        return ResponseEntity.of(anonymizationService.getAnonymizedDto(anonymizedId));
    }

    // 4. (Опционально) Получить оригинальные данные по анонимному ID (деобезличивание)
    // Ограничить доступ через Spring Security
    @GetMapping("/deanonymize/{anonymizedId}")
    public ResponseEntity<PersonalDataDTO> deanonymize(@PathVariable UUID anonymizedId) {
        return ResponseEntity.of(anonymizationService.getOriginalDtoByAnonymizedId(anonymizedId));
    }
    @GetMapping(("/all-original"))
    public ResponseEntity<List<PersonalDataEntity>> getAllOriginalData() {
        return ResponseEntity.ok(anonymizationService.getAllPersonalData());
    }
    @GetMapping("/all-link")
    public ResponseEntity<List<LinkTable>> getAllLinks() {
        return ResponseEntity.ok(anonymizationService.getAllLinks());
    }
    @GetMapping("/all-anonymized")
    public ResponseEntity<List<AnonymizedData>> getAllAnonymizedData() {
        return ResponseEntity.ok(anonymizationService.getAllAnonymizedData());
    }
    @GetMapping("/link/{id}")
    public ResponseEntity<LinkTable> getLinkById(@PathVariable UUID id) {
        return ResponseEntity.ok(anonymizationService.getLinkById(id));
    }

}
