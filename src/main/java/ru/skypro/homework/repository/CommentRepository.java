package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findCommentByAdId(Integer adId, Long commentId);

    void deleteByIdAndAdId(Integer adId, Long commentId);

}
