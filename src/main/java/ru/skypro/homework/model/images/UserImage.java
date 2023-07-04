package ru.skypro.homework.model.images;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.model.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserImage {
    @Id
    @OneToOne(targetEntity = User.class, mappedBy = "userId")
    private Integer UserId;
    private String imageAddress;

    public UserImage(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
