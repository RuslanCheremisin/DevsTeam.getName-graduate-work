package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.images.AdImage;
import ru.skypro.homework.model.images.UserImage;
import ru.skypro.homework.repository.AdImageRepository;
import ru.skypro.homework.repository.UserImageRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
@Service
public class ImageService {

    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    @Value("${path.to.user.images}/")
    private String pathToUserImages;

    private AdImageRepository adImageRepository;
    private UserImageRepository userImageRepository;

    public ImageService(AdImageRepository adImageRepository, UserImageRepository userImageRepository) {
        this.adImageRepository = adImageRepository;
        this.userImageRepository = userImageRepository;
    }

    public String updateImage(Integer id, MultipartFile file, boolean isUserImage) {
        File tempFile;
        String imageAddress = "";
        if (isUserImage) {
            tempFile = new File(pathToUserImages, id + "_user_image.jpg");
            imageAddress = pathToUserImages + "/" + id + "_user_image.jpg";
            UserImage image = new UserImage(imageAddress);
            userImageRepository.save(image);
        } else {
            tempFile = new File(pathToAdImages, id + "_ad_image.jpg");
            imageAddress = pathToAdImages + "/" + id + "_ad_image.jpg";
            AdImage image = new AdImage(imageAddress);
            adImageRepository.save(image);

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
}
