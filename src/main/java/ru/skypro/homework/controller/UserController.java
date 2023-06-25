package ru.skypro.homework.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;



@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {


/** 1. Изменение пароля пользователя */
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody PasswordDTO newPassword) {
        PasswordDTO passwordDTO = new PasswordDTO();
        return ResponseEntity.ok(passwordDTO);
    }


    /** 2. Получение информации о пользователе */
    @GetMapping("/me")
    public ResponseEntity<?> getUser() {
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok().body(userDTO);
    }


    /** 3. Изменение информации о пользователе */
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateReq userUpdateReq) {
        return ResponseEntity.ok().body(userUpdateReq);
    }


    /** 4. Загрузка аватара пользователя */
    @PatchMapping(value ="/me/image",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile file) {
        UserDTO userDTO = new UserDTO();
        return ResponseEntity.ok().build();
    }

}
