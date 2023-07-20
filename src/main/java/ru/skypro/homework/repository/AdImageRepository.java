package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.images.AdImage;

public interface AdImageRepository extends JpaRepository<AdImage, Integer> {
    AdImage findAdImageByImageAddress(String imageAddress);
}
