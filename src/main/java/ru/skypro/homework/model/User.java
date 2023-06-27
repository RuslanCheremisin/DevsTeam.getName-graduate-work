package ru.skypro.homework.model;

import ru.skypro.homework.dto.Role;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Column(name = "email")
    private String email;
    @Column(name = "firstName")
    private String firstName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "lastName")
    private String lastname;
    @Column(name = "phone")
    private String phone;
    @Column(name="Role")
    @Enumerated(EnumType.STRING)
    Role role;
    //OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    //List<Ad> ads = new ArrayList<>();
    @Column(name = "image")
    private String image;

    public User(String email, String firstName, Long id, String lastname, String phone, String image) {
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastname = lastname;
        this.phone = phone;
        this.image = image;
    }

    public User() {
    }

    public User(String email, String firstName, String lastname, String phone, String image) {
        this.email = email;
        this.firstName = firstName;
        this.lastname = lastname;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public void setImage(String image) {
        this.image = image;
    }
}
