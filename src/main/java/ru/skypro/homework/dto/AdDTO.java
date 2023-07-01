package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class AdDTO {
    Integer author;
    String image;
    Integer pk;
    Integer price;
    String title;

    public AdDTO(Integer author, String image, Integer pk, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
    }

    public AdDTO() {
    }
}
