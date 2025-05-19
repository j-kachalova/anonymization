package com.kachalova.fileprocessing.controller;

import com.kachalova.fileprocessing.dto.PersonalDataDTO;
import com.kachalova.fileprocessing.service.PersonalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-data")
@RequiredArgsConstructor
public class PersonalDataController {

    private final PersonalDataService service;

    @PostMapping
    public ResponseEntity<String> saveAndSend(@RequestBody PersonalDataDTO dto) {
        try {
            service.process(dto);
            return ResponseEntity.ok("✅ Сохранено и отправлено в Kafka");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Ошибка: " + e.getMessage());
        }
    }
}
