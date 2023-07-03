package ru.skypro.homework.dto;

import java.util.List;

public class Ads {
    int count;
    List<Ad> result;

    public Ads(int count, List<Ad> result) {
        this.count = count;
        this.result = result;
    }
}
