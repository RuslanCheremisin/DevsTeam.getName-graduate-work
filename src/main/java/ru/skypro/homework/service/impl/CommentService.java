package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /** Преобразование сущности Comment  в  DTO */
    public CommentDTO commentToCommentDTO(Comment comment) {
        return new CommentDTO(comment.getAuthor(), comment.getAuthorImage(), comment.getAuthorFirstName(),
                              comment.getCreatedAt(),
                              comment.getCommentId(),
                              comment.getText());
    }

    /** Преобразование DTO в сущность Comment  */
    public Comment commentDTOtoComment (CommentDTO commentDTO) {
        return new Comment(commentDTO.getCommentId(),
                commentDTO.getAuthor(), commentDTO.getAuthorImage(), commentDTO.getAuthorFirstName(),
                commentDTO.getCreatedAt(),
                commentDTO.getText());
    }

    /** Получение комментариев объявления */
    public CommentsDTO getCommentsOfAd(Integer adId) {
        List<Comment> commentList = commentRepository.findAllById(Collections.singleton(adId));
        List<CommentDTO> commentsDTOList = commentList.stream()
                .map(e -> commentToCommentDTO(e))
                .collect(Collectors.toList());
        CommentsDTO commentsDTO = new CommentsDTO(commentsDTOList.size(), commentsDTOList) ;
        return commentsDTO;
    }

    /** Добавление комментария к объявлению */
    public CommentDTO addCommentToAd(Integer adId,CommentDTO comment) {


        return null;
    }

    /** Удаление комментария. */
    public boolean deleteCommentById(Integer adId, Integer commentId){
        return false;
    }

    /** Обновление комментария */
    public boolean updateCommentById(Integer adId, Integer commentId, CreateOrUpdateComment newText) {
        return false;
    }


}
