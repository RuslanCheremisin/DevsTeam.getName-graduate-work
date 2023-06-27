package ru.skypro.homework.dto;

import lombok.Data;


@Data
public class UserDTO {
    int id;
    String email;
    String firstName;
    String lastname;
    String phone;
    String image;
}
