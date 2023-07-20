package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Integer id);

    Optional<User> findUserByPassword(String password);
}
