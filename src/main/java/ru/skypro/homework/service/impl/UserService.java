package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.images.UserImage;
import ru.skypro.homework.repository.UserImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final JpaUserDetailsManager detailsManager;

    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final UserImageRepository userImageRepository;


    public UserService(UserRepository userRepository, JpaUserDetailsManager usersManager, PasswordEncoder passwordEncoder, ImageService imageService, UserImageRepository userImageRepository) {
        this.userRepository = userRepository;
        this.detailsManager = usersManager;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.userImageRepository = userImageRepository;
    }

    /**
     * Получает авторизированного пользователя и возвращает его
     *
     * @return авторизированный пользователь
     */
    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findUserByUsername(currentPrincipalName).orElseThrow();
    }

    /**
     * Преобразование сущности User  в  DTO
     *
     * @param user объект пользователь из БД
     * @return объект UserDTO
     */
    public UserDTO userToUserDTO(User user) {
        if (user.getImage() != null) {
            return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getPhone(),
                    user.getImage().getImageAddress());
        } else {
            return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getPhone(),
                    null);
        }
    }


    /**
     * Обновление пароля пользователя
     *
     * @param passwordDTO новый пароль
     */
    public boolean updateUserPassword(PasswordDTO passwordDTO) throws UnauthorizedException {
        User user = getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        detailsManager.changePassword(user.getPassword(), passwordEncoder.encode(passwordDTO.getNewPassword()));
        return true;
    }


    /**
     * Обновление данных пользователя
     *
     * @param req
     */
    public void updateUser(UserUpdateReq req) throws UnauthorizedException {
        User user = getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setPhone(req.getPhone());
        detailsManager.updateUser(user);
    }

    /**
     * Получает авторизированного пользователя
     *
     * @return объект UserDTO
     */
    public UserDTO getUser() throws UnauthorizedException {
        User user = getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        return userToUserDTO(user);
    }


    /**
     * Принимает файл автара и сохраняет его, ссылку на аватар добавляет в БД
     *
     * @param file новый автар
     * @return
     */
    public boolean updateUserImage(MultipartFile file) throws UnauthorizedException {
        User user = getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        imageService.updateImage(user.getId(), file, true);
//        Integer userId = user.getUserId();
//        Path path = Paths.get(Path.of("./").toAbsolutePath().getParent().getParent().getParent().toString()+"/user_images/");
//        if(!Files.exists(path)){
//           new File(path.toString()).mkdir();
//        }
//        File tempFile = new File(path.toString(), String.valueOf(userId)+".jpg");
//        try (OutputStream os = new FileOutputStream(tempFile)) {
//            os.write(file.getBytes());
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        UserImage image = new UserImage(user, "/users/avatar/" + userId);
//        userImageRepository.save(image);
//        user.setImage(image);
//        userRepository.save(user);
        return true;
    }


}
