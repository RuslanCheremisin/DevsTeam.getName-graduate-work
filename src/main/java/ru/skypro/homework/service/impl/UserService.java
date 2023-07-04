package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserDetailsManager detailsManager;

    private final PasswordEncoder passwordEncoder;




    public UserService(UserRepository userRepository, UserDetailsManager usersManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.detailsManager = usersManager;
        this.passwordEncoder = passwordEncoder;

    }

    /**
     * Получает авторизированного пользователя и возвращает его
     * @return авторизированный пользователь
     */
    public User getAuthUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findUserByEmail(currentPrincipalName);
    }

    /**
     * Преобразование сущности User  в  DTO
     * @param user объект пользователь из БД
     * @return объект UserDTO
     */
    public UserDTO userToUserDTO(User user){
        return new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhone(),
                user.getImage());
    }

    /**
     * Создание пользователя при регистрации
     * @param req минимальные данные для регистрации
     * @return User
     */
    public User registerReqToUser(RegisterReq req){
    return new User(req.getUsername(), passwordEncoder.encode(req.getPassword()), req.getFirstName(), req.getLastName(),
            req.getPhone(), req.getRole());
    }

    /**
     * Сохраняет пользователя после регистрации в БД
     * @param req регистрационные данные
     * @param role роль
     */
    public void saveRegisteredUser(RegisterReq req, Role role){
        User user = registerReqToUser(req);
        user.setRole(role);
        userRepository.save(user);
    }
    /**
     * Обновление пароля пользователя
     * @param passwordDTO   новый пароль
     *
     */
    public boolean updateUserPassword(PasswordDTO passwordDTO) throws UnauthorizedException {
        User user = getAuthUser();
        if (user == null){
            throw new UnauthorizedException();
        }
        String username = user.getEmail();
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
        UserDetails userDetails = detailsManager.loadUserByUsername(username);
        detailsManager.changePassword((userDetails.getPassword()), passwordEncoder.encode(passwordDTO.getNewPassword()));
        return true;
    }


    /**
     * Обновление данных пользователя
     * @param req
     */
    public void updateUser(UserUpdateReq req) throws UnauthorizedException {
        User user = getAuthUser();
        if (user == null){
            throw new UnauthorizedException();
        }
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setPhone(req.getPhone());
        userRepository.save(user);
    }

    /**
     * Получает авторизированного пользователя
     * @return объект UserDTO
     */
    public UserDTO getUser() throws UnauthorizedException {
        User user = getAuthUser();
        if (user == null){
            throw new UnauthorizedException();
        }
        return userToUserDTO(user);
    }


    /**
     * Принимает файл автара и сохраняет его, ссылку на аватар добавляет в БД
     * @param file новый автар
     * @return
     */
    public boolean updateUserImage(MultipartFile file) throws UnauthorizedException {
        User user = getAuthUser();
        if (user == null){
            throw new UnauthorizedException();
        }
        Integer userId = user.getUserId();
        File tempFile = new File("src/main/resources/user_images/", String.valueOf(userId)+".jpg");
        try (OutputStream os = new FileOutputStream(tempFile)) {
            os.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.setImage("/users/avatar/"+userId);
        userRepository.save(user);
        return true;
    }

    /**
     *  Отдает автар пользователя в виде массива байтов
     * @param id Идентификатор пользователя
     * @return массив байтов
     * @throws IOException
     */
    public byte[] getUserImage (Integer id) throws IOException {
        User user = userRepository.findById(id).orElseThrow();
        return Files.readAllBytes(Path.of("src/main/resources/user_images/"+user.getUserId()+".jpg"));
    }
}
