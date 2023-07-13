package ru.skypro.homework.model;

import lombok.extern.apachecommons.CommonsLog;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.NaturalId;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.images.UserImage;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
    private String username;
    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<AuthGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Ad> ads =new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Comment> comments =new ArrayList<>();


    @OneToOne(targetEntity = UserImage.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_image_id")
    private UserImage image;



    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }



    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = (Set<AuthGrantedAuthority>) authorities;
    }



    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



    public User(String username, String password, Set<AuthGrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }



    public User(Integer userId, String username, String firstName, String lastname, String phone, UserImage image) {
        this.id = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastname;
        this.phone = phone;
        this.image = image;
    }

    public User(String username, String password, String firstName, String lastName, String phone, Set<AuthGrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.authorities = authorities;
    }

    public User() {
    }


    public User(String username, String firstName, String lastname, String phone, UserImage image) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastname;
        this.phone = phone;
        this.image = image;
    }



    public User(String username, String password, String firstName, String lastName, String phone
    ) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public UserImage getImage() {
        return image;
    }

    public void setImage(UserImage image) {
        this.image = image;
    }
}
