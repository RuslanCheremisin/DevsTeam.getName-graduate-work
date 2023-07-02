package ru.skypro.homework.model;

import javax.persistence.*;

@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private Integer author;
    private String image;

    private String description;
    private Integer price;
    private String title;

    public Ad(Integer author, String image, Integer pk, String description, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.description = description;
        this.price = price;
        this.title = title;
    }

    public Ad(Integer author, String description, String image, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.description = description;
        this.price = price;
        this.title = title;
    }

    public Ad() {
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
