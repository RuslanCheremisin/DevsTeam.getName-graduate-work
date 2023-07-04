package ru.skypro.homework.model.images;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.model.Ad;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AdImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(targetEntity = Ad.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "ad_id")
    private Ad ad;
    private String imageAddress;

    public AdImage(String imageAddress){
        this.imageAddress = imageAddress;
    }
}
