package ru.skypro.homework.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer commentId;
    Integer adId;
    Integer author;
    String authorImage;
    String authorFirstName;
    Long createdAt;
    String text;

    // Constructors ------------------------------------

    public Comment() {
    }

    public Comment(Integer commentId, Integer adId,
                   Integer author, String authorImage, String authorFirstName,
                   Long createdAt, String text) {
        this.commentId = commentId;
        this.adId = adId;
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.text = text;
    }

    public Comment(Integer commentId,
                   Integer author, String authorImage, String authorFirstName,
                   Long createdAt, String text) {
        this.commentId = commentId;
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.text = text;
    }

    // Get & Set ---------------------------------------

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentId, comment.commentId)
                && Objects.equals(adId, comment.adId)
                && Objects.equals(author, comment.author)
                && Objects.equals(authorImage, comment.authorImage)
                && Objects.equals(authorFirstName, comment.authorFirstName)
                && Objects.equals(createdAt, comment.createdAt)
                && Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, adId, author, authorImage, authorFirstName, createdAt, text);
    }
}
