package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.images.AdImage;
import ru.skypro.homework.model.images.UserImage;
import ru.skypro.homework.repository.AdImageRepository;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class ImageService {

    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    @Value("${path.to.user.images}/")
    private String pathToUserImages;

    private AdImageRepository adImageRepository;
    private UserImageRepository userImageRepository;

    private UserRepository userRepository;
    private AdRepository adRepository;

    public ImageService(AdImageRepository adImageRepository, UserImageRepository userImageRepository, UserRepository userRepository, AdRepository adRepository) {
        this.adImageRepository = adImageRepository;
        this.userImageRepository = userImageRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    public String updateUserImage(String username, MultipartFile file) {
        init();
        User user = userRepository.findUserByUsername(username).orElseThrow();
        String imageAddress = "/users/avatar/" + user.getId();
        if (user.getImage() == null) {
            UserImage image = new UserImage(user, imageAddress);
            userImageRepository.save(image);
            user.setImage(image);
            userRepository.save(user);
        }
        File tempFile = new File(
                Path.of(pathToUserImages).toString(),
                username + "_user_image.jpg");
        writeFile(tempFile, file);
        return imageAddress;
    }

    public String updateAdImage(Integer id, MultipartFile file) {
        Ad ad = adRepository.findById(id).orElseThrow();
        String imageAddress = "/ads/image/" + id;
        AdImage image = new AdImage(ad, imageAddress);
        adImageRepository.save(image);
        ad.setImage(image);
        File tempFile = new File(
                Path.of(pathToAdImages).toString(),
                adImageRepository.findAdImageByImageAddress(imageAddress).getId() + "_ad_image.jpg");
        adRepository.save(ad);
        writeFile(tempFile, file);
        return imageAddress;
    }

    private void writeFile(File tempFile, MultipartFile file) {
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found!");
        } catch (
                IOException e) {
            throw new RuntimeException();
        }
    }

/** Получение аватара пользователя */
    public FileSystemResource getUserImage(Integer id) throws IOException {
        User user = userRepository.findUserById(id).orElseThrow();
        return new FileSystemResource(Path.of(pathToUserImages + user.getUsername() + "_user_image.jpg"));
    }

    /** Получение картинки объявления */
    public FileSystemResource getAdImage(Integer id) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow();
        AdImage image = ad.getImage();
        return new FileSystemResource(Path.of(pathToAdImages + image.getId() + "_ad_image.jpg"));
    }

    private void init() {
        File adImagesDir = new File(pathToAdImages);
        File userImagesDir = new File(pathToUserImages);
        if (!adImagesDir.exists()) {
            adImagesDir.mkdirs();
        }
        if (!userImagesDir.exists()) {
            userImagesDir.mkdirs();
        }
    }
}
