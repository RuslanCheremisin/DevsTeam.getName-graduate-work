package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.impl.UserService;



@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** 1. Изменение пароля пользователя */
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody PasswordDTO newPassword) throws UnauthorizedException {
        userService.updateUserPassword(newPassword);
        return ResponseEntity.ok().build();
    }


    /** 2. Получение информации о пользователе */
    @GetMapping("/me")
    public ResponseEntity<?> getUser() throws UnauthorizedException {
        UserDTO userDTO = userService.getUser();
        return ResponseEntity.ok().body(userDTO);
    }


    /** 3. Изменение информации о пользователе */
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateReq userUpdateReq) throws UnauthorizedException {
        userService.updateUser(userUpdateReq);
        return ResponseEntity.ok().body(userUpdateReq);
    }


    /** 4. Загрузка аватара пользователя */
    @PatchMapping(value ="/me/image",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile file) throws UnauthorizedException {
        userService.updateUserImage(file);
        return ResponseEntity.ok().build();
    }


}
