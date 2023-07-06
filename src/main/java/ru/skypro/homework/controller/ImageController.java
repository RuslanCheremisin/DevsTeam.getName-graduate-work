package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.impl.AdService;

import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
public class ImageController {
    private final AdService adService;

    public ImageController(AdService adService) {
        this.adService = adService;
    }

    @PatchMapping("ads/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable Integer id,
                                         @RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok().body(adService.updateAdImage(id, file));
    }

    /** Отдает массив байтов по ссылке на картинку объявления */
    @GetMapping(value ="ads//images/{id}")
    public ResponseEntity<FileSystemResource> getAdImage(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(adService.getAdImage(id));
    }
}
