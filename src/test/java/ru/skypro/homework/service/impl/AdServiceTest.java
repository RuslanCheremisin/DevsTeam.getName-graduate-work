package ru.skypro.homework.service.impl;


import lombok.With;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.UserPrincipal;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdServiceTest {


    @Value("${name.of.test.data.file}")
    private String testFileName;


    @Autowired
    AdRepository adRepository;

    @Autowired
    AdService adService;

    @Autowired
    UserService userService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @BeforeEach
    public void setUp() {
        authService.register(new RegisterReq("user@gmail.com", "password", "Ivan",
                "Petrov", "+7900000000", Role.USER), Role.USER);
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void getAdByIdReturnsCorrectExistingAd() throws IOException, UnauthorizedException {
        adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Ad ad = adService.getAdById(1);
        Assertions.assertEquals("title", ad.getTitle());
        Assertions.assertEquals("description", ad.getDescription());
        Assertions.assertEquals(100, ad.getPrice());
    }

    @Test
    public void getAdByIdWithNotExistingAdThrowsException() {
        Assertions.assertThrows(NoSuchElementException.class, () -> adService.getAdById(1));
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void getAdsReturnsAdsListIfAdsExist() throws IOException, UnauthorizedException {
        adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        adService.addAd(new CreateOrUpdateAd("title1", "description1", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Assertions.assertTrue(adService.getAds().getCount() == 2);
    }

    @Test
    public void getAdsReturnEmptyListIfAdsNotExist() {
        Assertions.assertTrue(adService.getAds().getCount() == 0);
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void getAdReturnsCorrectExtendedAd() throws IOException, UnauthorizedException {
        adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Ad ad = adService.getAdById(1);
        ExtendedAd extendedAd = adService.getAd(1);
        Assertions.assertEquals( ad.getPk(),extendedAd.getPk());
        Assertions.assertEquals( ad.getAuthor().getFirstName(),extendedAd.getAuthorFirstName());
        Assertions.assertEquals( ad.getAuthor().getLastName(),extendedAd.getAuthorLastName());
        Assertions.assertEquals( ad.getDescription(),extendedAd.getDescription());
        Assertions.assertEquals( ad.getAuthor().getUsername(),extendedAd.getEmail());
        Assertions.assertEquals( ad.getPrice(),extendedAd.getPrice());
        Assertions.assertEquals( ad.getTitle(),extendedAd.getTitle());
        Assertions.assertEquals( ad.getAuthor().getPhone(),extendedAd.getPhone());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void addAddCorrectlyCreatesNewAddWithValidData() throws IOException, UnauthorizedException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Assertions.assertEquals("title", adDTO.getTitle());
        Assertions.assertEquals(100, adDTO.getPrice());
        Assertions.assertEquals(1, adDTO.getAuthor());
        Assertions.assertEquals(1, adDTO.getPk());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void addAddDoesNotCreatesNewAddWithEmptyTitle() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.addAd(new CreateOrUpdateAd("",
                        "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName))));
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void addAddDoesNotCreatesNewAddWithEmptyDescription() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.addAd(new CreateOrUpdateAd("title",
                        "", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName))));
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void addAddDoesNotCreatesNewAddWithBellowZeroPrice() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.addAd(new CreateOrUpdateAd("title",
                        "description", -49),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName))));
    }


    @Test
    @WithMockUser("user@gmail.com")
    public void adToAdDTOreturnsCorrectAdDTO(){
        User user = userService.getAuthUser();
        Ad ad = new Ad(user, new Image("imageName"), 1,"description", 333, "title");
        AdDTO adDTO = adService.adToDTO(ad);
        Assertions.assertEquals(ad.getPk(), adDTO.getPk());
        Assertions.assertEquals("/ads/image/1", adDTO.getImage());
        Assertions.assertEquals(ad.getTitle(), adDTO.getTitle());
        Assertions.assertEquals(ad.getPrice(), adDTO.getPrice());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void adToExtendedAdReturnsCorrectExtendedAd(){
        User user = userService.getAuthUser();
        Ad ad = new Ad(user, new Image("imageName"), 1,"description", 333, "title");
        ExtendedAd extendedAd = adService.adTOExtended(ad);
        Assertions.assertEquals("/ads/image/1", extendedAd.getImage());
        Assertions.assertEquals( ad.getPk(),extendedAd.getPk());
        Assertions.assertEquals( ad.getAuthor().getFirstName(),extendedAd.getAuthorFirstName());
        Assertions.assertEquals( ad.getAuthor().getLastName(),extendedAd.getAuthorLastName());
        Assertions.assertEquals( ad.getDescription(),extendedAd.getDescription());
        Assertions.assertEquals( ad.getAuthor().getUsername(),extendedAd.getEmail());
        Assertions.assertEquals( ad.getPrice(),extendedAd.getPrice());
        Assertions.assertEquals( ad.getTitle(),extendedAd.getTitle());
        Assertions.assertEquals( ad.getAuthor().getPhone(),extendedAd.getPhone());
    }


    @Test
    @WithMockUser("user@gmail.com")
    public void getAllUsersAdReturnsAllExistsUsersAds() throws IOException, UnauthorizedException {
        User user = userService.getAuthUser();
        adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        adService.addAd(new CreateOrUpdateAd("title2", "description2", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Assertions.assertEquals(2, adService.getAllUserAdds().getResults().stream()
                .filter(a -> a.getAuthor() == user.getId()).collect(Collectors.toList()).size());

    }

    @Test
    @WithMockUser("user@gmail.com")
    public void getAllUsersAdReturnsEmptyListIfThereIsNoThisUsersAds() {
        User authUser = userService.getAuthUser();
        User newUser = new User("user2@gmail.com","password", "Fedor",
                "Sinicin", "+8444");
        newUser.setRole(Role.USER);
        User savedNewUser = userRepository.save(newUser);
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(savedNewUser, "descr", savedImage,
                100, "title"));
        Assertions.assertEquals(0, adService.getAllUserAdds().getCount());

    }


    @Test
    @WithMockUser("user@gmail.com")
    public void getAllUsersAdReturnsEmptyListIfThereAreNoAds() {
        Assertions.assertEquals(0, adService.getAllUserAdds().getCount());
        Assertions.assertTrue(adService.getAllUserAdds().getResults().isEmpty());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAuthorAndRequestIsCorrect() throws IOException, UnauthorizedException {
        User user = userService.getAuthUser();
       adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("title2", "desc2", 1000);
        AdDTO updatedAd = adService.updateAdInfo(1, createOrUpdateAd);
        Assertions.assertEquals(user.getId(), updatedAd.getAuthor());
        Assertions.assertEquals(createOrUpdateAd.getTitle(), updatedAd.getTitle());
        Assertions.assertEquals(createOrUpdateAd.getPrice(), updatedAd.getPrice());
    }

    @Test
    @WithMockUser("admin@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAdminAndRequestIsCorrect() {
        authService.register(new RegisterReq("admin@gmail.com", "password", "Semen",
                "Lavrov", "+7900000000", Role.ADMIN), Role.ADMIN);
        User newUser = new User("user2@gmail.com","password", "Fedor",
                "Sinicin", "+8444");
        newUser.setRole(Role.USER);
        User savedNewUser = userRepository.save(newUser);
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(savedNewUser, "descr", savedImage,
                100, "title"));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("title2", "desc2", 1000);
        AdDTO updatedAd = adService.updateAdInfo(1, createOrUpdateAd);
        Assertions.assertEquals(newUser.getId(), updatedAd.getAuthor());
        Assertions.assertEquals(createOrUpdateAd.getTitle(), updatedAd.getTitle());
        Assertions.assertEquals(createOrUpdateAd.getPrice(), updatedAd.getPrice());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsNotAuthorAndRequestIsCorrect()  {
        User newUser = new User("user2@gmail.com","password", "Fedor",
                "Sinicin", "+8444");
        newUser.setRole(Role.USER);
        User savedNewUser = userRepository.save(newUser);
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(savedNewUser, "descr", savedImage,
                100, "title"));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("title2", "desc2", 1000);
        AdDTO updatedAd = adService.updateAdInfo(1, createOrUpdateAd);
        Assertions.assertEquals(newUser.getId(), updatedAd.getAuthor());
        Assertions.assertEquals("title", updatedAd.getTitle());
        Assertions.assertEquals(100, updatedAd.getPrice());
    }


    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAuthorAndTitleIsEmpty() throws IOException, UnauthorizedException {
        User user = userService.getAuthUser();
        adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("", "desc2", 1000);
        Assertions.assertThrows(IllegalArgumentException.class, ()->adService.updateAdInfo(1, createOrUpdateAd));
    }


    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAuthorAndDescriptionIsEmpty() throws IOException, UnauthorizedException {
        User user = userService.getAuthUser();
        adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("title", "", 1000);
        Assertions.assertThrows(IllegalArgumentException.class, ()->adService.updateAdInfo(1, createOrUpdateAd));
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAuthorAndPriceIsBellowZero() throws IOException, UnauthorizedException {
        adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("title", "desc2", -1000);
        Assertions.assertThrows(IllegalArgumentException.class, ()->adService.updateAdInfo(1, createOrUpdateAd));
    }

}
