package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.images.UserImage;

public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
    UserImage findUserImageByImageAddress(String imageAddress);
}
