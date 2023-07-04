package ru.skypro.homework.model;

import lombok.Data;
import ru.skypro.homework.model.images.AdImage;

import javax.persistence.*;
@Data
@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private AdImage image;

    private String description;
    private Integer price;
    private String title;

    public Ad(User author, AdImage image, Integer pk, String description, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.description = description;
        this.price = price;
        this.title = title;
    }

    public Ad(User author, String description, AdImage image, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.description = description;
        this.price = price;
        this.title = title;
    }

    public Ad() {
    }

}
