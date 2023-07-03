package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;
@Data
public class AdsGetResp {
    int count;
    List<Ad> result;
}