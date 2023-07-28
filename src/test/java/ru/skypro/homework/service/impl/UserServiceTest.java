package ru.skypro.homework.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.UserPrincipal;
import ru.skypro.homework.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.NoSuchElementException;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Value("${name.of.test.data.file}")
    private String testFileName;


    @Autowired
    AuthServiceImpl authService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @BeforeEach
    public void init(){
        authService.register(new RegisterReq("user@gmail.com", "password", "Ivan",
                "Petrov", "+7900000000", Role.USER), Role.USER);
    }
    @Test
    @WithMockUser("user@gmail.com")
    public void getAuthUserIsNotNull(){
        User user = userService.getAuthUser();
        Assertions.assertEquals("user@gmail.com",user.getUsername());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void userToUserDTOreturnsCorrectDTOwithCorrectUser(){
        User user = userService.getAuthUser();
        UserDTO userDTO = userService.userToUserDTO(userService.getAuthUser());
        Assertions.assertEquals(user.getUsername(), userDTO.getEmail());
        Assertions.assertEquals( user.getFirstName(), userDTO.getFirstName());
        Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        Assertions.assertEquals(user.getPhone(),userDTO.getPhone());
        Assertions.assertEquals(user.getRole(),userDTO.getRole());
    }

    @Test
    public void userToUserDTOthrowsExceptionWithoutUsername(){
        User user = new User(1,"", "Ivan", "Petrov", "+789444", Role.USER, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }

    @Test
    public void userToUserDTOthrowsExceptionWithoutFirstName(){
        User user = new User(1,"user", "", "Petrov", "+789444", Role.USER, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }

    @Test
    public void userToUserDTOthrowsExceptionWithoutLastName(){
        User user = new User(1,"user", "Ivan", "", "+789444", Role.USER, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }


    @Test
    public void userToUserDTOthrowsExceptionWithoutPhone(){
        User user = new User(1,"user", "Ivan", "Petrov", "", Role.USER, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }

    @Test
    public void userToUserDTOthrowsExceptionWithoutRole(){
        User user = new User(1,"user", "Ivan", "Petrov", "45555", null, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void updatesPasswordWithCorrectData(){
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("password");
        passwordDTO.setNewPassword("password1");
        Assertions.assertTrue(userService.updateUserPassword(passwordDTO));
    }

    @Test
    @WithMockUser(value = "user@gmail.com")
    public void doesNotUpdatePasswordWithNullCurrentPassword(){
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword(null);
        passwordDTO.setNewPassword("password2");
        Assertions.assertFalse(userService.updateUserPassword(passwordDTO));
    }

    @Test
    @WithMockUser(value = "user@gmail.com")
    public void doesNotUpdatePasswordWithEmptyCurrentPassword(){
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("");
        passwordDTO.setNewPassword("password2");
        Assertions.assertFalse(userService.updateUserPassword(passwordDTO));
    }

    @Test
    @WithMockUser(value = "user@gmail.com")
    public void doesNotUpdatePasswordWithNullNewPassword(){
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("password");
        passwordDTO.setNewPassword(null);
        Assertions.assertFalse(userService.updateUserPassword(passwordDTO));
    }
    @Test
    @WithMockUser(value = "user@gmail.com")
    public void doesNotPpdatePasswordWithEmptyNewPassword(){
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("password");
        passwordDTO.setNewPassword(null);
        Assertions.assertFalse(userService.updateUserPassword(passwordDTO));
    }

    @Test
    @WithMockUser(value = "user@gmail.com")
    public void updatesUserInfoWithCorrectData() throws UnauthorizedException {
        UserUpdateReq userUpdateReq = new UserUpdateReq("Semen", "Ivanov", "911");
        userService.updateUser(userUpdateReq);
        User updatedUser = userRepository.findUserByUsername("user@gmail.com").orElseThrow();
        Assertions.assertEquals(userUpdateReq.getFirstName(), updatedUser.getFirstName());
        Assertions.assertEquals(userUpdateReq.getLastName(), updatedUser.getLastName());
        Assertions.assertEquals(userUpdateReq.getPhone(), updatedUser.getPhone());
    }

    @Test
    @WithMockUser(value = "user@gmail.com")
    public void getUserReturnsCorrectUserDTO() throws UnauthorizedException {
        User  user = userService.getAuthUser();
        UserDTO userDTO = userService.getUser();
        Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        Assertions.assertEquals(user.getUsername(), userDTO.getEmail());
        Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        Assertions.assertEquals(Role.USER, userDTO.getRole());
    }

    @Test
    @WithMockUser(value = "user@gmail.com")
    public void updateUserImageWorksWithCorrectImage() throws UnauthorizedException, IOException {
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", new FileInputStream(testFileName));
        userService.updateUserImage(multipartFile);
        Assertions.assertFalse(userService.getAuthUser().getImage()==null);
    }

    @Test
    public void loadUserByUsernameReturnsCorrectUserPrincipal(){
        UserDetails userPrincipal = userService.loadUserByUsername("user@gmail.com");
        Assertions.assertEquals("user@gmail.com", userPrincipal.getUsername());
        Assertions.assertEquals(Collections.singleton("ROLE_USER").toString(), userPrincipal.getAuthorities().toString());
    }


    @Test
    public void loadUserByUsernameReturnsWithNotExistingUsernameThrowsException(){
        Assertions.assertThrows(NoSuchElementException.class, ()->userService.loadUserByUsername("user1@gmail.com"));

    }

    @Test
    public void userExistsReturnsTrueIfUserExists(){
        Assertions.assertTrue(userService.userExists("user@gmail.com"));
    }

    @Test
    public void userExistsReturnsFalseIfUserNotExists(){
        Assertions.assertFalse(userService.userExists("user1@gmail.com"));
    }
}
