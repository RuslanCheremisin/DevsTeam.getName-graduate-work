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
@Table(name = "ad_images")
public class AdImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(targetEntity = Ad.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ad_id")
    private Ad ad;
    private String imageName;

    public AdImage(Ad ad, String imageAddress){
        this.ad = ad;
        this.imageName = imageAddress;
    }
}
