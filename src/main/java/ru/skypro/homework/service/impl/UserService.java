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

    public UserDTO userToUserDTO(User user){
        return new UserDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getLastname(), user.getPhone(),
                user.getImage());
    }
    public User registerReqToUser(RegisterReq req){
    return new User(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(),
            req.getPhone(), req.getRole());
    }

    public void updateUser(UserUpdateReq req){
    }

    public void updatePassword(PasswordDTO passwordDTO){}


}
