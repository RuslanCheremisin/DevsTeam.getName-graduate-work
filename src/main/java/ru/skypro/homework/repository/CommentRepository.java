package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findCommentByAdIdAndCommentId(Integer adId, Integer commentId);

    List<Comment> findCommentsByAdId(Integer adId);

    void deleteByAdIdAndCommentId(Integer adId, Integer commentId);

}
