package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
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

    @Operation(summary = "Получить все объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "ОК",
                    content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        AdsGetResp resp = new AdsGetResp();
        resp.setResult(new ArrayList<>());
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "Получить информацию об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "ОК",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
            @ApiResponse(responseCode = "404",
                    description = "Not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getAdById(@PathVariable Integer id) {
        ExtendedAdDTO extendedAdDTO = new ExtendedAdDTO();
        return ResponseEntity.ok().body(extendedAdDTO);
    }

    @Operation(summary = "Добавить оъявление")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Created",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized")
    })
    @PostMapping()
    public ResponseEntity<?> addAd(@RequestBody AdsAddReq req) {
        AdDTO adDTO = new AdDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(adDTO);
    }

    @Operation(summary = "Удалить объявление")
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    description = "No content"
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden"),
            @ApiResponse(responseCode = "404",
                    description = "Not found"
            )
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/{id}/comments")
    @Tags(
            @Tag(name = "Комментарии")
    )
    @Operation(
            summary = "Получить комментарии объявления",
            description = "Получение всех комментариев по id объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    }
    )
    public ResponseEntity<Comments> getComments(@PathVariable(name = "id") int adId) {
        return ResponseEntity.ok().body(new Comments());
    }

    @PostMapping("/{id}/comments")
    @Tags(
            @Tag(name = "Комментарии")
    )
    @Operation(
            summary = "Добавление комментария",
            description = "Добавление комментария по id объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    }
    )
    public ResponseEntity<CommentDTO> addComment(@PathVariable(name = "id") int adId, @RequestBody CommentDTO comment) {
        return ResponseEntity.ok(new CommentDTO());
    }

    @GetMapping("/{id}")
    @Tags(
            @Tag(name = "Объявления")
    )
    @Operation(
            summary = "Получить информацию об объявлении",
            description = "Получение объявления по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    }
    )
    public ResponseEntity<ExtendedAdDTO> getAds(@PathVariable(name = "id") int adId) {
        return ResponseEntity.ok().body(new ExtendedAdDTO());
    }

    @DeleteMapping("/{id}")
    @Tags(
            @Tag(name = "Объявления")
    )
    @Operation(
            summary = "Удалить объявление",
            description = "Удаление объявления по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "No content"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )

    }
    )
    public ResponseEntity<?> removeAd(@PathVariable(name = "id") int adId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


