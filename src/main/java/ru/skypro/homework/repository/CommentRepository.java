package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Comment;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentByAdIdAndCommentId(Integer adId, Long commentId);

    List<Comment> findCommentsByAdId(Integer adId);

    void deleteByAdIdAndCommentId(Integer adId, Long commentId);

}
