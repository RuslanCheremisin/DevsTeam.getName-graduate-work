package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CommentDTO {
    Integer author;
    String authorImage;
    String authorFirstName;
    Long createdAt;
    Integer idCom;
    String text;
}
