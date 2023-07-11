package ru.skypro.homework.model;

import lombok.Data;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.images.UserImage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Ad> ads = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(targetEntity = UserImage.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_image_id")
    private UserImage image;

    public User(String email, String password, String firstName, String lastName, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }

    public User(Integer userId, String email, String firstName, String lastname, String phone, UserImage image) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastname;
        this.phone = phone;
        this.image = image;
    }

    public User() {
    }

    public User(String email, String firstName, String lastname, String phone, UserImage image) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastname;
        this.phone = phone;
        this.image = image;
    }

//    public List<Comment> getComments() {
//        return comments;
//    }
//
//    public void setAds(List<Ad> ads) {
//        this.ads = ads;
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public List<Ad> getAds() {
//        return ads;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer id) {
//        this.userId = id;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public UserImage getImage() {
//        return image;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    public void setImage(UserImage image) {
//        this.image = image;
//    }
//
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(userId, email, password, firstName, lastName, phone, ads, comments, role, image);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o;
//        return Objects.equals(userId, user.userId) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phone, user.phone) && Objects.equals(ads, user.ads) && Objects.equals(comments, user.comments) && role == user.role && Objects.equals(image, user.image);
//    }
}
