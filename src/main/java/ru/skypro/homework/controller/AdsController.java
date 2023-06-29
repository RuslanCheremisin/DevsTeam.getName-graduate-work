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
import ru.skypro.homework.service.impl.CommentService;

import java.util.ArrayList;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final CommentService commentService;

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
    public ResponseEntity<?> getAds(@PathVariable Integer adId) {
        ExtendedAdDTO extendedAdDTO = new ExtendedAdDTO();
        return ResponseEntity.ok().body(extendedAdDTO);
    }

    /** 10. Удаление объявления */
    @DeleteMapping("{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer adId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /** 11. Получение комментариев объявления */
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDTO> getComments(@PathVariable(name = "id") Integer adId) {
        return ResponseEntity.ok().body(commentService.getCommentsOfAd(adId));
    }
    /** 12. Добавление комментария к объявлению */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable(name = "id") Integer adId,
                                                 @RequestBody CommentDTO comment) {
        return ResponseEntity.ok(new CommentDTO());
    }

    /** 13. Обновление информации об объявлении */
    @PatchMapping("{id}")
    public ResponseEntity<?> updateAds(@PathVariable Integer adId,
                                       @RequestBody CreateOrUpdateAd newAdReg) {
        return ResponseEntity.ok().body(newAdReg);
    }

    /** 14. Удаление комментария. */
    @DeleteMapping("{id}/comments/{idCom}")
    public ResponseEntity<?> deleteComments(@PathVariable Integer adId,
                                            @PathVariable Integer commentId){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /** 15. Обновление комментария */
    @PatchMapping("{id}/comments/{idCom}")
    public ResponseEntity<?> updateComments(@PathVariable Integer adId,
                                            @PathVariable Integer commentId,
                                            @RequestBody CreateOrUpdateComment newText) {
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
    public ResponseEntity<?> updateImage(@PathVariable Integer adId,
                                         @RequestBody String newPart) {
        return ResponseEntity.ok().body(newPart);
    }

}


