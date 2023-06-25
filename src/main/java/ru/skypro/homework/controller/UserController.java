package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.File;


@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {


/**Изменение пароля пользователя */
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody String newPassword) {
        PasswordDTO passwordDTO = new PasswordDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(passwordDTO);
    }


    /**Получение информации о пользователе */
    @GetMapping("/me")
    public ResponseEntity<?> getUser() {
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok().body(userDTO);
    }


    /**Изменение информации о пользователе */
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok().body(userDTO);
    }


    /**Загрузка аватара пользователя */
    @PatchMapping(value ="/me/image",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile file) {
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok().build();
    }

}
