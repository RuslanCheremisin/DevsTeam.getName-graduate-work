package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.impl.AdService;
import ru.skypro.homework.service.impl.ImageService;

import java.io.IOException;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
public class ImageController {
    private final AdService adService;
    private final ImageService imageService;

    public ImageController(AdService adService, ImageService imageService) {
        this.adService = adService;
        this.imageService = imageService;
    }

    @PatchMapping("ads/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable Integer id,
                                         @RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok().body(adService.updateAdImage(adService.getAdById(id), file));
    }

    /**
     * Отдает массив байтов по ссылке на картинку объявления
     */
    @GetMapping(value ="ads/image/{id}")
    public FileSystemResource getAdImage(@PathVariable Integer id) throws IOException {
        return imageService.getAdImage(id);
    }

    @GetMapping(value = "users/avatar/{id}")
    public FileSystemResource getUserImage(@PathVariable Integer id) throws IOException {
        return imageService.getUserImage(id);
    }


}
