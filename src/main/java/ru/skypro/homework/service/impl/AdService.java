package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdService {
    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    private final AdRepository adRepository;

    private final UserRepository userRepository;
    private final UserService userService;

    private final ImageService imageService;

    public AdService(AdRepository adRepository, UserRepository userRepository, UserService userService, ImageService imageService) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.imageService = imageService;
    }

    public Ads getAds() {
        List<Ad> adsList = adRepository.findAll();
        return new Ads(adsList.size(), adsList.stream().map(this::adToDTO).collect(Collectors.toList()));
    }

    public ExtendedAd getAd(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow();
        return adTOExtended(ad);
    }

    public AdDTO addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile file) throws UnauthorizedException {
        User user = userService.getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Ad ad = new Ad(user, createOrUpdateAd.getDescription(), "image", createOrUpdateAd.getPrice(), createOrUpdateAd.getTitle());
        Ad savedAd = adRepository.save(ad);

        imageService.updateImage(ad.getPk(),file,false);


        File tempFile = new File(pathToAdImages, savedAd.getPk() + "_ad_image.jpg");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found!");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        savedAd.setImage(pathToAdImages + savedAd.getPk() + "_ad_image.jpg");

        return adToDTO(adRepository.save(savedAd));
    }

    private AdDTO adToDTO(Ad ad) {
        return new AdDTO(
                ad.getAuthor().getUserId(),
                ad.getImage(),
                ad.getPk(),
                ad.getPrice(),
                ad.getTitle());
    }

    private ExtendedAd adTOExtended(Ad ad) {
        return new ExtendedAd(
                ad.getPk(),
                ad.getAuthor().getFirstName(),
                ad.getAuthor().getLastName(),
                ad.getDescription(),
                ad.getAuthor().getEmail(),
                ad.getImage(),
                ad.getAuthor().getPhone(),
                ad.getPrice(),
                ad.getTitle());
    }

    private void init() {
        try {
            Files.createDirectories(Path.of(pathToAdImages));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось инициализировать директорию для выгрузки файла!");
        }
    }

    public Ads getAllUserAdds() {
        User user = userService.getAuthUser();
        List<AdDTO> list = user.getAds().stream().map(this::adToDTO).collect(Collectors.toList());
        return new Ads(list.size(), list);
    }

    public FileSystemResource getAdImage(Integer id) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow();
        return new FileSystemResource(Path.of(pathToAdImages + ad.getPk() + "_ad_image.jpg"));
    }


    public AdDTO updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        Ad ad = adRepository.findById(id).orElseThrow();
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        return adToDTO(adRepository.save(ad));
    }

    public void removeAd(Integer id) {
        adRepository.deleteById(id);
    }

    public AdDTO updateAdImage(Integer id, MultipartFile file) {
        Ad ad = adRepository.findById(id).orElseThrow();
        File tempFile = new File(pathToAdImages, ad.getPk() + "_ad_image.jpg");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found!");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return adToDTO(ad);
    }
}
