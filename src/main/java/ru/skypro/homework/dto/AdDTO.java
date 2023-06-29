package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class AdDTO {
    Integer author;
    String image;
    Integer adId;
    Integer price;
    String title;
}
