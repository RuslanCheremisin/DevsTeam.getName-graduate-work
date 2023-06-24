package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

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
    public ResponseEntity<?> getAdById(@PathVariable Integer id) {
        ExtendedAdDTO extendedAdDTO = new ExtendedAdDTO();
        return ResponseEntity.ok().body(extendedAdDTO);
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


    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable(name = "id") int adId) {
        return ResponseEntity.ok().body(new Comments());
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable(name = "id") int adId, @RequestBody CommentDTO comment) {
        return ResponseEntity.ok(new CommentDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDTO> getAds(@PathVariable(name = "id") int adId) {
        return ResponseEntity.ok().body(new ExtendedAdDTO());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable(name = "id") int adId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


