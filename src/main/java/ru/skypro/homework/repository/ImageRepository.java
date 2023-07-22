package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.images.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image findAdImageByImageName(String imageAddress);
}
