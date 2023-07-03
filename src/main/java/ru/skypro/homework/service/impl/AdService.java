package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdService {
    @Value("${path.to.ad.images}")
    private String pathToAdImages;

    private final AdRepository adRepository;

    private final UserService userService;

    public AdService(AdRepository adRepository, UserService userService) {
        this.adRepository = adRepository;
        this.userService = userService;
    }

    public Ads getAds(){
        List<ru.skypro.homework.model.Ad> adsList = adRepository.findAll();
        Ads ads = new Ads(adsList.size(), adsList.stream().map(this::adToDTO).collect(Collectors.toList()));
        return ads;
    }

    public ExtendedAdDTO getAd(Integer id){
        ru.skypro.homework.model.Ad ad = adRepository.findById(id).get();
        return adTOExtended(ad);
    }

    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile file) throws UnauthorizedException {
        User user = userService.getAuthUser();
        if(user==null){
            throw new UnauthorizedException();
        }
        ru.skypro.homework.model.Ad ad = new ru.skypro.homework.model.Ad(
                user.getUserId(),
                createOrUpdateAd.getDescription(),
                "image", createOrUpdateAd.getPrice(),
                createOrUpdateAd.getTitle());

        File tempFile = new File(pathToAdImages, ad.getPk() + "_ad_image.jpeg");
        try(FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch(FileNotFoundException e){
            throw new RuntimeException("File not found!");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        ad.setImage(tempFile.getPath());
        return adToDTO(adRepository.save(ad));
    }

    private Ad adToDTO(ru.skypro.homework.model.Ad ad){
        return new Ad(
                ad.getAuthor(),
                ad.getImage(),
                ad.getPk(),
                ad.getPrice(),
                ad.getTitle());
    }

    private ExtendedAdDTO adTOExtended(ru.skypro.homework.model.Ad ad){
        User user = userService.getAuthUser();
        return new ExtendedAdDTO(
                ad.getPk(),
                user.getFirstName(),
                user.getLastName(),
                ad.getDescription(),
                user.getEmail(),
                ad.getImage(),
                user.getPhone(),
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


}
