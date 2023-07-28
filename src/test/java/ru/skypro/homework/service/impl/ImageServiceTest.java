package ru.skypro.homework.service.impl;

import org.assertj.core.util.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@SpringBootTest
public class ImageServiceTest {
    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    @Value("${path.to.user.images}/")
    private String pathToUserImages;
    @Value("${name.of.test.data.file}")
    private String testFileName;

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageService imageService;

    @Autowired
    AuthServiceImpl authService;
    @Mock
    User user;
    @Mock
    MultipartFile imageFile;

    @BeforeEach
    public void init() {
        user = new User("user@gmail.com", "password", "Ivan", "Petrov", "+7900000000", Role.USER);
        userRepository.save(user);
        imageFile = new MockMultipartFile(testFileName, new byte[]{1});
    }

    @Test
    public void updateUserImageCreatesUserImageFileInTheFolder() {

        imageService.updateUserImage(user.getUsername(), imageFile);
        Image image = user.getImage();
        FileSystemResource resource = new FileSystemResource(pathToUserImages + image.getImageName());
        Assertions.assertTrue(resource.exists());
    }


}
