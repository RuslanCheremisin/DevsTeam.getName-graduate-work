package ru.skypro.homework.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.dto.AdsAddReq;
import ru.skypro.homework.dto.AdsGetResp;

import java.util.ArrayList;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {

    /** 7. Получение всех объявлений */
    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        AdsGetResp resp = new AdsGetResp();
        resp.setResult(new ArrayList<>());
        return ResponseEntity.ok().body(resp);
    }

    /** 8. Добавление объявления */
    @PostMapping()
    public ResponseEntity<?> addAd(@RequestBody AdsAddReq req) {
        AdDTO adDTO = new AdDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(adDTO);
    }
    /** 9. Получение информации об объявлении */
    @GetMapping("{id}")
    public ResponseEntity<?> getAds(@PathVariable Integer id) {
        ExtendedAdDTO extendedAdDTO = new ExtendedAdDTO();
        return ResponseEntity.ok().body(extendedAdDTO);
    }

    /** 10. Удаление объявления */
    @DeleteMapping("{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


