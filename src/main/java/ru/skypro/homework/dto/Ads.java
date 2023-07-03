package ru.skypro.homework.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
public class Ads {
    Integer count;
    List<AdDTO> result;

    public Ads(List<AdDTO> result) {
        this.count = result.size();
        this.result = result;
    }

}
