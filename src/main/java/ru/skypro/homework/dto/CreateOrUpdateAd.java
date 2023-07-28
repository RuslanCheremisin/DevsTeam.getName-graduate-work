package ru.skypro.homework.dto;

import liquibase.pro.packaged.A;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrUpdateAd {

    String title;
    String description;
    Integer price;

}
