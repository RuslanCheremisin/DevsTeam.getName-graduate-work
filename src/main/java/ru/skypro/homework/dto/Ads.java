package ru.skypro.homework.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//@Data
@NoArgsConstructor
public class Ads {
    private Integer count;
    private List<AdDTO> result;

    public Ads(Integer count, List<AdDTO> result) {
        this.count = count;
        this.result = result;
    }

}
