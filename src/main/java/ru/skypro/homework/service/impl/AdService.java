package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
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

    public AdService(AdRepository adRepository, UserRepository userRepository, UserService userService) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public AdsDTO getAds(){
        List<Ad> adsList = adRepository.findAll();
        return new AdsDTO(adsList.stream().map(this::adToDTO).collect(Collectors.toList()));
    }

    public ExtendedAd getAd(int id){
        Ad ad = adRepository.findById(id).orElseThrow();
        return adTOExtended(ad);
    }

    public AdDTO addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile file) throws UnauthorizedException {
        User user = userService.getAuthUser();
        if(user==null){
            throw new UnauthorizedException();
        }
        Ad ad = new Ad(user, createOrUpdateAd.getDescription(), "image", createOrUpdateAd.getPrice(), createOrUpdateAd.getTitle());
        Ad savedAd = adRepository.save(ad);
        File tempFile = new File(pathToAdImages, savedAd.getPk() + "_ad_image.jpg");
        try(FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch(FileNotFoundException e){
            throw new RuntimeException("File not found!");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        savedAd.setImage("/ads/images/"+savedAd.getPk());

        return adToDTO(adRepository.save(savedAd));
    }

    private AdDTO adToDTO(Ad ad){
        return new AdDTO(
                ad.getAuthor().getUserId(),
                ad.getImage(),
                ad.getPk(),
                ad.getPrice(),
                ad.getTitle());
    }

    private ExtendedAd adTOExtended(Ad ad){
        return new ExtendedAd(
                ad.getPk(),
                ad.getAuthor().getFirstName(),
                ad.getAuthor().getLastName(),
                ad.getDescription(),
                ad.getAuthor().getEmail(),
                ad.getImage(),
                ad.getAuthor().getPhone(),
                ad.getPrice(),
                ad.getTitle());}

    private void init() {
        try {
            Files.createDirectories(Path.of(pathToAdImages));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось инициализировать директорию для выгрузки файла!");
        }
    }

    public AdsDTO getAllUserAdds(){
        User user = userService.getAuthUser();
        List<AdDTO> list =adRepository.findAdsByAuthor(user).stream().map(this::adToDTO).collect(Collectors.toList());
        return new AdsDTO(list);
    }

    public byte[] getAdImage (Integer id) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow();
        return Files.readAllBytes(Path.of(pathToAdImages+ad.getPk()+"_ad_image.jpg"));
    }


    public AdDTO updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd){
        Ad ad = adRepository.findById(id).orElseThrow();
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        return adToDTO(adRepository.save(ad));
    }

    public void removeAd(Integer id){
        adRepository.deleteById(id);
    }

    public AdDTO updateAdImage(Integer id, MultipartFile file) {
        Ad ad = adRepository.findById(id).orElseThrow();
        File tempFile = new File(pathToAdImages, ad.getPk() + "_ad_image.jpg");
        try(FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch(FileNotFoundException e){
            throw new RuntimeException("File not found!");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return adToDTO(ad);
    }

    public AdsDTO searchAds(String req){
        List<Ad> list = adRepository.findAdsByTitleContaining(req);
        return new AdsDTO(list.stream().map(this::adToDTO).collect(Collectors.toList()));
    }
}
