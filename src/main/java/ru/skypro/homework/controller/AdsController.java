package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.util.ArrayList;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
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

    /** 13. Обновление информации об объявлении */
    @PatchMapping("{id}")
    public ResponseEntity<?> updateAds(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd newAdReg) {
        return ResponseEntity.ok().body(newAdReg); //new AdsAddReq()
    }

    /** 14. Удаление комментария. */
    @DeleteMapping("{id}/comments/{idCom}")
    public ResponseEntity<?> deleteComments(@PathVariable Integer id,
                                            @PathVariable Long idCom){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /** 15. Обновление комментария */
    @PatchMapping("{id}/comments/{idCom}")
    public ResponseEntity<?> updateComments(@PathVariable Integer id,
                                            @PathVariable Long idCom,
                                            @RequestBody CreateOrUpdateComment newText) {  //CreateOrUpdateComment -> CommentDTO()
        return ResponseEntity.ok().body(newText);
    }

    /** 16. Получение объявлений авторизованного пользователя */
    @GetMapping("/me")
    public ResponseEntity<?> getAdsMe() {
        AdsGetResp adsGetResp = new AdsGetResp();
        return ResponseEntity.ok().body(adsGetResp);
    }

    /** 17. Обновление картинки объявления */
    @PatchMapping("{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable Integer id,
                                         @RequestBody String newPart) {
        return ResponseEntity.ok().body(newPart);
    }

}


