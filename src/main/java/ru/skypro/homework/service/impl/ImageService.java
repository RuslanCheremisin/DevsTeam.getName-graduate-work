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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public String updateImage(Integer id, MultipartFile file, boolean isUserImage) {
        init();
        File tempFile;
        String imageAddress = null;
        if (isUserImage) {
            User user = userRepository.findById(id).orElseThrow();
            imageAddress = "/users/avatar/" + id;
            UserImage image = new UserImage(user, imageAddress);
            userImageRepository.save(image);
            tempFile = new File(
                    Path.of(pathToUserImages).toAbsolutePath().toFile(),
                    userImageRepository.findByImageAddress(imageAddress).getId() + "_user_image.jpg");
            user.setImage(image);
            userRepository.save(user);
        } else {
            Ad ad = adRepository.findById(id).orElseThrow();
            imageAddress = "ads/image/" + id;
            AdImage image = new AdImage(ad, imageAddress);
            adImageRepository.save(image);
            ad.setImage(image);
            tempFile = new File(
                    Path.of(pathToAdImages).toAbsolutePath().toFile(),
                    adImageRepository.findAdImageByImageAddress(imageAddress).getId() + "_ad_image.jpg");
            adRepository.save(ad);
        }
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found!");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return imageAddress;
    }

    public byte[] getUserImage(Integer id) throws IOException {
        User user = userRepository.findById(id).orElseThrow();
        UserImage image = user.getImage();
        return Files.readAllBytes(Path.of(pathToUserImages + image.getId() + "_user_image.jpg"));
    }

    public byte[] getAdImage(Integer id) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow();
        AdImage image = ad.getImage();
        return Files.readAllBytes(Path.of(pathToAdImages + image.getId() + "_ad_image.jpg"));
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
