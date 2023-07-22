package ru.skypro.homework.model;

import lombok.Data;
import ru.skypro.homework.model.images.AdImage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(targetEntity = AdImage.class, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private AdImage image;

    private String description;
    private Integer price;
    private String title;
    @OneToMany(mappedBy = "ad", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> comments =new ArrayList<>();

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
