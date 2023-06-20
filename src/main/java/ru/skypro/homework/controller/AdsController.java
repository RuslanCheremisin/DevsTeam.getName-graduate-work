package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdFullDTO;
import ru.skypro.homework.dto.AdsAddReq;
import ru.skypro.homework.dto.AdsGetResp;

import java.util.ArrayList;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {


    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        AdsGetResp resp = new AdsGetResp();
        resp.setResult(new ArrayList<>());
            return ResponseEntity.ok().body(resp);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        AdFullDTO adFullDTO = new AdFullDTO();
        return ResponseEntity.ok().body(adFullDTO);
    }
    @PostMapping()
    public ResponseEntity<?> addAd(@RequestBody AdsAddReq req) {
        AdDTO adDTO = new AdDTO();
            return ResponseEntity.status(HttpStatus.CREATED).body(adDTO);
        }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


