package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.AdRepository;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skypro.homework.utils.AuthorizationUtils.isUserAdAuthorOrAdmin;

@Service
public class AdService {
    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    private final AdRepository adRepository;
    private final UserService userService;

    private final ImageService imageService;

    private final ImageRepository imageRepository;

    public AdService(AdRepository adRepository, UserService userService, ImageService imageService, ImageRepository imageRepository) {
        this.adRepository = adRepository;
        this.userService = userService;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    public Ad getAdById(Integer id){
        return adRepository.findById(id).orElseThrow();
    }
    public AdsDTO getAds() {
        List<Ad> adsList = adRepository.findAll();
        return new AdsDTO(adsList.stream().map(this::adToDTO).collect(Collectors.toList()));
    }

    public ExtendedAd getAd(int id) {
        Ad ad = adRepository.findById(id).orElseThrow();
        return adTOExtended(ad);
    }

    public AdDTO addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile file) throws UnauthorizedException {
        User user = userService.getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Ad ad = new Ad(user, createOrUpdateAd.getDescription(), null, createOrUpdateAd.getPrice(), createOrUpdateAd.getTitle());
        Ad savedAd = adRepository.save(ad);

        imageService.updateAdImage(savedAd.getPk(), file);
        return adToDTO(adRepository.save(savedAd));
    }

    private AdDTO adToDTO(Ad ad) {
        return new AdDTO(
                ad.getAuthor().getId(),
                "/ads/image/"+ad.getPk(),
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
                ad.getAuthor().getUsername(),
                "/ads/image/"+ad.getPk(),
                ad.getAuthor().getPhone(),
                ad.getPrice(),
                ad.getTitle());
    }

      public AdsDTO getAllUserAdds() {
        User user = userService.getAuthUser();
        List<AdDTO> list = adRepository.findAdsByAuthor(user).stream().map(this::adToDTO).collect(Collectors.toList());
        return new AdsDTO(list);
    }


    public AdDTO updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        User user = userService.getAuthUser();
        Ad ad = getAdById(id);
        if(isUserAdAuthorOrAdmin(ad, user)){
            ad.setDescription(createOrUpdateAd.getDescription());
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());}
        return adToDTO(adRepository.save(ad));

    }
    public void removeAd(Integer id) {
        User user = userService.getAuthUser();
        Ad ad = getAdById(id);
        if(isUserAdAuthorOrAdmin(ad, user)){
        adRepository.delete(ad);}
    }

    public AdDTO updateAdImage(Integer adId, MultipartFile file) {
        User user = userService.getAuthUser();
        Ad ad = getAdById(adId);
        if(isUserAdAuthorOrAdmin(ad, user)){
        File tempFile = new File(pathToAdImages, ad.getImage().getImageName());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found!");
        } catch (IOException e) {
            throw new RuntimeException();
        }}
        return adToDTO(ad);
    }

    public AdsDTO searchAds(String req) {
        List<Ad> list = adRepository.findAdsByTitleContaining(req);
        return new AdsDTO(list.stream().map(this::adToDTO).collect(Collectors.toList()));
    }
}
