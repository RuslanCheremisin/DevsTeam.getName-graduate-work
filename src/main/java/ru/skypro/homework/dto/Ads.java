package ru.skypro.homework.dto;

import java.util.List;

public class Ads {
    int count;
    List<AdDTO> result;

    public Ads(int count, List<AdDTO> result) {
        this.count = count;
        this.result = result;
    }
}
