package ru.skypro.homework.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity

public class AuthGrantedAuthority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String authority;

    @ManyToOne
    @JoinColumn(name = "auth_user_detail_id")
    private User user;

    public AuthGrantedAuthority(Long id) {
        this.id = id;
    }

    public AuthGrantedAuthority() {

    }

    public AuthGrantedAuthority(String authority, User user) {
        this.authority = authority;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
//constructors
    //getters and setters


}
