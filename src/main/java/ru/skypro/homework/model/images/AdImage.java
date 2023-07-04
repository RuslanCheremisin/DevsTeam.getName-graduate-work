package ru.skypro.homework.model.images;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.model.Ad;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AdImage {
    @Id
    @OneToOne(targetEntity = Ad.class, mappedBy = "pK")
    private Integer id;
    private String imageAddress;

    public AdImage(String imageAddress){
        this.imageAddress = imageAddress;
    }
}
