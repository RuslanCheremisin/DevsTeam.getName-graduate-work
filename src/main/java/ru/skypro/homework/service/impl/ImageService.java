package ru.skypro.homework.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    @Value("${path.to.user.images}/")
    private String pathToUserImages;

    private ImageRepository imageRepository;

    private UserRepository userRepository;
    private AdRepository adRepository;

    public ImageService(ImageRepository imageRepository, UserRepository userRepository, AdRepository adRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    public void updateUserImage(String username, MultipartFile file) {
        init();
        User user = userRepository.findUserByUsername(username).orElseThrow();
        String imageName = generateRandomFileName(file);
        if (user.getImage() == null) {
            Image image = new Image(imageName);
            imageRepository.save(image);
            user.setImage(image);
            userRepository.save(user);
        }
        else {
            Image image = user.getImage();
            deleteAvatarIfExists(image.getImageName());
            image.setImageName(imageName);
            imageRepository.save(image);
        }
        File tempFile = new File(
                Path.of(pathToUserImages).toAbsolutePath().toFile(),
                imageName);
        writeFile(tempFile, file);
    }

    public void updateAdImage(Integer id, MultipartFile file) {
        init();
        Ad ad = adRepository.findById(id).orElseThrow();
        String imageName = generateRandomFileName(file);
        Image image = new Image(imageName);
        ad.setImage(image);
        imageRepository.save(image);
        File tempFile = new File(
                Path.of(pathToAdImages).toAbsolutePath().toString(),
                imageName);
        adRepository.save(ad);
        writeFile(tempFile, file);
    }
    private void deleteAvatarIfExists(String fileName){
        Path path = Paths.get(Path.of(pathToUserImages).toAbsolutePath().toString(),
                fileName);

        try {
            boolean result = Files.deleteIfExists(path);
            if (result) {
                System.out.println("File is successfully deleted.");
            }
            else {
                System.out.println("File deletion failed.");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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


    private String generateRandomFileName(MultipartFile file) {
        String imageName= UUID.randomUUID() + "."+FilenameUtils.getExtension(file.getOriginalFilename());
        while(imageRepository.findAdImageByImageName(imageName)!=null){
            imageName =  UUID.randomUUID() + "."+FilenameUtils.getExtension(file.getOriginalFilename());
        }
        return imageName;
    }

    public FileSystemResource getUserImage(Integer id) {
        User user = userRepository.findUserById(id).orElseThrow();
        return new FileSystemResource(Path.of(pathToUserImages, user.getImage().getImageName()));
    }

    public FileSystemResource getAdImage(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow();
        Image image = ad.getImage();
        return new FileSystemResource(Path.of(pathToAdImages, image.getImageName()));
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
