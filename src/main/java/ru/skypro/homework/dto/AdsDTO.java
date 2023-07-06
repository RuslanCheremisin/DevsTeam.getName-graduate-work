package ru.skypro.homework.dto;


import lombok.Data;

import java.util.List;
@Data
public class AdsDTO {
    Integer count;
    List<AdDTO> results;

    public AdsDTO(List<AdDTO> results) {
        this.count = results.size();
        this.results = results;
    }

}
