package ru.skypro.homework.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
public class Ads {
    Integer count;
    List<AdDTO> results;

    public Ads(List<AdDTO> results) {
        this.count = results.size();
        this.results = results;
    }

}
