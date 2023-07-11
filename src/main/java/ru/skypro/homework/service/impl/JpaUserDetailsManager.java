package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.PasswordDTO;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.model.AuthGrantedAuthority;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class JpaUserDetailsManager implements UserDetailsManager {



    @Autowired
    private  UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("No user found with username = " + username));
    }


    @Override
    public void createUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void deleteUser(String username) {
        User userDetails = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No User found for username -> " + username));
        userRepository.delete(userDetails);
    }

    /**
     * This method assumes that both oldPassword and the newPassword params
     * are encoded with configured passwordEncoder
     *
     * @param oldPassword the old password of the user
     * @param newPassword the new password of the user
     */
    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        User userDetails = userRepository.findUserByPassword(oldPassword)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid password "));
        userDetails.setPassword(newPassword);
        userRepository.save(userDetails);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }
}