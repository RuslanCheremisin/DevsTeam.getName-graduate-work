package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    /** 1. Преобразование сущности Comment  в  DTO */
    public CommentDTO commentToCommentDTO(Comment comment) {
        return new CommentDTO(comment.getAuthor(), comment.getAuthorImage(), comment.getAuthorFirstName(),
                              comment.getCreatedAt(),
                              comment.getCommentId(),
                              comment.getText());
    }

    /** 2. Преобразование DTO в сущность Comment  */
    public Comment commentDTOtoComment (CommentDTO commentDTO) {
        return new Comment(commentDTO.getPk(),
                commentDTO.getAuthor(), commentDTO.getAuthorImage(), commentDTO.getAuthorFirstName(),
                commentDTO.getCreatedAt(),
                commentDTO.getText());
    }

    /** 3. Получение комментариев объявления */
    public CommentsDTO getCommentsOfAd(Integer adId) {
        List<Comment> commentList = commentRepository.findCommentsByAdId(adId);
        List<CommentDTO> commentsDTOList = commentList.stream()
                .map(e -> commentToCommentDTO(e))
                .collect(Collectors.toList());
        return new CommentsDTO(commentsDTOList.size(), commentsDTOList);
    }

    /** 4. Добавление комментария к объявлению */
    public CommentDTO addCommentToAd(Integer adId,CreateOrUpdateComment textComment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findUserByEmail(currentPrincipalName);
        Comment comment = new Comment(adId,
                user.getUserId(),
                user.getImage(),
                user.getFirstName(),
                Instant.now().toEpochMilli(), //дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
                textComment.getText());
        commentRepository.save(comment);
        return commentToCommentDTO(comment);
    }

    /** 5. Удаление комментария. */
    public void deleteCommentById(Integer adId, Integer commentId){
        Comment comment = commentRepository.findCommentByAdIdAndCommentId(adId, commentId);
        if (comment != null) {
            commentRepository.delete(comment);
        } else throw new RuntimeException("Такой комментарий не найден");
    }

    /** 6. Обновление комментария */
    public CommentDTO updateCommentById(Integer adId, Integer commentId, CreateOrUpdateComment newText) {
        Comment comment = commentRepository.findCommentByAdIdAndCommentId(adId, commentId);
        if (comment != null) {
            comment.setText(newText.getText());
            commentRepository.save(comment);
          return commentToCommentDTO(comment);
        } else throw new RuntimeException("Такой комментарий не найден");
    }


}
