package ru.skypro.homework.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.PasswordDTO;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.dto.UserUpdateReq;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Преобразование сущности User  в  DTO
     * @param user объект пользователь из БД
     * @return объект UserDTO
     */
    public UserDTO userToUserDTO(User user){
        return new UserDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getLastname(), user.getPhone(),
                user.getImage());
    }

    /**
     * Создание пользователя при регистрации
     * @param req минимальные данные для регистрации
     * @return User
     */
    public User registerReqToUser(RegisterReq req){
    return new User(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(),
            req.getPhone(), req.getRole());
    }

    /**
     * Обновление данных пользователя
     * @param req
     */
    public void updateUser(UserUpdateReq req){
    }

    /**
     * Обновление пароля пользователя
     * @param passwordDTO старый пароль +  новый пароль
     */
    public void updatePassword(PasswordDTO passwordDTO){}


}
