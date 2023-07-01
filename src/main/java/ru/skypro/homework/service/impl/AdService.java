package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

import java.io.FileOutputStream;
import java.io.IOException;
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
        List<Ad> adsList = adRepository.findAll();
        Ads ads = new Ads(adsList.size(), adsList.stream().map(this::adToDTO).collect(Collectors.toList()));
        return ads;
    }

    public ExtendedAdDTO getAd(Integer id){
        Ad ad = adRepository.findById(id).get();
        return adTOExtended(ad);
    }

    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile file){
        User user = userService.getAuthUser();
        Ad ad = new Ad(user.getUserId(), "image", createOrUpdateAd.getDescription(), createOrUpdateAd.getPrice(), createOrUpdateAd.getTitle());
        try {
            init();
            byte[] bytes = file.getBytes();
            FileOutputStream fos = new FileOutputStream(pathToAdImages+"/"+ad.getPk()+"_ad_image.jpeg");
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ad.setImage(pathToAdImages+"/"+ad.getPk()+"_ad_image.jpeg");
        return adRepository.save(ad);
    }

    private AdDTO adToDTO(Ad ad){
        return new AdDTO(
                ad.getAuthor(),
                ad.getImage(),
                ad.getPk(),
                ad.getPrice(),
                ad.getTitle());
    }

    private ExtendedAdDTO adTOExtended(Ad ad){
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
