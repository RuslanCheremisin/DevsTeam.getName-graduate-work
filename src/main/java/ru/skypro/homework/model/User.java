package ru.skypro.homework.model;

import ru.skypro.homework.dto.Role;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private Role role;
    //OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    //List<Ad> ads = new ArrayList<>();
    private String image;
    public User(String email, String password, String firstName, String lastName, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }
    public User(Integer userId, String email, String firstName, String lastname, String phone, String image) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastname;
        this.phone = phone;
        this.image = image;
    }
    public User() {
    }
    public User(String email, String firstName, String lastname, String phone, String image) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastname;
        this.phone = phone;
        this.image = image;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer id) {
        this.userId = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
