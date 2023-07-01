package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class ExtendedAdDTO {
    Integer pk;
    String authorFirstName;
    String authorLastName;
    String description;
    String email;
    String image;
    String phone;
    Integer price;
    String title;


    public ExtendedAdDTO(Integer pk, String authorFirstName, String authorLastName, String description, String email, String image, String phone, Integer price, String title) {
        this.pk = pk;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.description = description;
        this.email = email;
        this.image = image;
        this.phone = phone;
        this.price = price;
        this.title = title;
    }

    public ExtendedAdDTO() {
    }
}
