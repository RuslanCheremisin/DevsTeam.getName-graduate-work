package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.UserPrincipal;
import ru.skypro.homework.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.NoSuchElementException;

import static ru.skypro.homework.utils.ValidationUtils.isNotEmptyAndNotNull;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
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
        if (user.getId() > 0 && isNotEmptyAndNotNull(user.getUsername()) && isNotEmptyAndNotNull(user.getFirstName())
                && isNotEmptyAndNotNull(user.getLastName()) && user.getRole() != null & isNotEmptyAndNotNull(user.getPhone())) {
            if (user.getImage() != null) {
                return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getRole(), user.getPhone(),
                        "/users/avatar/" + user.getId());
            } else {
                return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getRole(), user.getPhone(),
                        null);
            }
        }
        throw new IllegalArgumentException();
    }


    /**
     * Обновление пароля пользователя
     *
     * @param passwordDTO новый пароль
     */
    @Transactional
    public boolean updateUserPassword(PasswordDTO passwordDTO) {
        if (isNotEmptyAndNotNull(passwordDTO.getNewPassword()) && isNotEmptyAndNotNull(passwordDTO.getCurrentPassword())) {
            User user = getAuthUser();
            user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
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
        userRepository.save(user);
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
        imageService.updateUserImage(user.getUsername(), file);
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(NoSuchElementException::new);
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(user.getUsername(), user.getPassword(), user.getId(), user.getRole());
        return new UserPrincipal(userDetailsDTO);
    }

    public boolean userExists(String userName) {
        return userRepository.findUserByUsername(userName).isPresent();
    }
}
