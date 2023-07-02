package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.service.impl.AdService;
import ru.skypro.homework.service.impl.CommentService;

import java.util.ArrayList;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final CommentService commentService;
    private final AdService adService;

    /** 7. Получение всех объявлений */
    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok().body(adService.getAds());
    }

    /** 8. Добавление объявления */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@RequestParam MultipartFile photo, @RequestBody CreateOrUpdateAd createOrUpdateAd) throws UnauthorizedException {
//        AdDTO adDTO = new AdDTO();

        return ResponseEntity.ok(adService.addAd(createOrUpdateAd, photo));
    }
    /** 9. Получение информации об объявлении */
    @GetMapping("{id}")
    public ResponseEntity<?> getAd(@PathVariable Integer id) {
        return ResponseEntity.ok().body(adService.getAd(id));
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
                                                 @RequestBody CreateOrUpdateComment text) {
        return ResponseEntity.ok().body(commentService.addCommentToAd(adId, text));
    }

    /** 13. Обновление информации об объявлении */
    @PatchMapping("{id}")
    public ResponseEntity<?> updateAds(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd newAdReg) {
        return ResponseEntity.ok().body(newAdReg);
    }

    /** 14. Удаление комментария. */
    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComments(@PathVariable Integer adId,
                                            @PathVariable Integer commentId){
        commentService.deleteCommentById(adId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /** 15. Обновление комментария */
    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> updateComments(@PathVariable Integer adId,
                                            @PathVariable Integer commentId,
                                            @RequestBody CreateOrUpdateComment newText) {
        return ResponseEntity.ok().body(commentService.updateCommentById(adId, commentId, newText));
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


