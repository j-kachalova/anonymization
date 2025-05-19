package com.kachalova.fileprocessing.controller;

import com.kachalova.fileprocessing.kafka.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileProcessingService fileProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("inputTopic") String inputTopic) {
        try {
            fileProcessingService.processFile(file, inputTopic);
            return ResponseEntity.ok("Файл успешно обработан и отправлен в топик: " + inputTopic);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла: " + e.getMessage());
        }
    }

}

