package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.File;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {


    @Tags(
            @Tag(name = "Пользователи")
    )
    @Operation(
            summary = "setPassword",
            description = "установка пароля"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json")}
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
    @PostMapping("/users/set_password")
    public ResponseEntity<?> setPassword(@RequestBody String newPassword) {
        PasswordDTO passwordDTO = new PasswordDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(passwordDTO);
    }

    @Tags(
            @Tag(name = "Пользователи")
    )
    @Operation(
            summary = "getUser",
            description = "получение пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json")}
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
    @GetMapping("/users/me")
    public ResponseEntity<?> getUser() {
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok().body(userDTO);
    }

    @Tags(
            @Tag(name = "Пользователи")
    )
    @Operation(
            summary = "updateUser",
            description = "обновление данных пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json")}
            ),
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
    @PatchMapping("/users/me")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok().body(userDTO);
    }
    @Tags(
            @Tag(name = "Пользователи")
    )
    @Operation(
            summary = "updateUserImage",
            description = "updateUserImage"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found"
            )
    }
    )
    @PatchMapping(value ="/users/me/image",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("file") MultipartFile file) {
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok().build();
    }

}
