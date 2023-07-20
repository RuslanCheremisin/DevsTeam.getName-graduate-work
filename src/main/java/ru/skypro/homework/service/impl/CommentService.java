package ru.skypro.homework.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skypro.homework.utils.AuthorizationUtils.isUserCommentAuthorOrAdmin;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;

    private final UserService userService;

    public CommentService(CommentRepository commentRepository, AdRepository adRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.userService = userService;
    }

    /**
     * 1. Преобразование сущности Comment  в  DTO
     */
    public CommentDTO commentToCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getAuthor().getId(),
                comment.getAuthor().getImage() == null ? null : comment.getAuthor().getImage().getImageAddress(),
                comment.getAuthor().getFirstName(),
                comment.getCreatedAt(),
                comment.getCommentId(),
                comment.getText());
    }


    /**
     * 2. Получение комментариев объявления
     */
    public CommentsDTO getCommentsOfAd(Integer adId) {
        Ad ad = adRepository.findById(adId).orElseThrow();
        List<Comment> commentList = commentRepository.findCommentsByAd(ad);
        List<CommentDTO> commentsDTOList = commentList.stream()
                .map(e -> commentToCommentDTO(e))
                .collect(Collectors.toList());
        return new CommentsDTO(commentsDTOList.size(), commentsDTOList);
    }

    /**
     * 3. Добавление комментария к объявлению
     */

    public CommentDTO addCommentToAd(Integer adId, CreateOrUpdateComment textComment) {
        User user = userService.getAuthUser();
        Ad ad = adRepository.findById(adId).orElseThrow();
        Comment comment = new Comment(ad,
                user,
                Instant.now().toEpochMilli(), //дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
                textComment.getText());
        commentRepository.save(comment);
        return commentToCommentDTO(comment);
    }

    /**
     * 4. Удаление комментария.
     */

    public void deleteCommentById(Integer adId, Integer commentId) {
        User user = userService.getAuthUser();
        Comment comment = getCommentByAdIdAndCommentID(adId, commentId);
        if(isUserCommentAuthorOrAdmin(comment, user)){
            commentRepository.delete(comment);
        } else throw new RuntimeException("Такой комментарий не найден");
    }

    /**
     * 5. Обновление комментария
     */

    public CommentDTO updateCommentById(Integer adId, Integer commentId, CreateOrUpdateComment newText) {
        User user = userService.getAuthUser();
        Comment comment = getCommentByAdIdAndCommentID(adId, commentId);
        if(isUserCommentAuthorOrAdmin(comment, user)){
            comment.setText(newText.getText());
            commentRepository.save(comment);
            return commentToCommentDTO(comment);
        } else throw new RuntimeException("Такой комментарий не найден");
    }
    /**
     * 6. Получение комментария по ID объявления и ID комментария
     */
    public Comment getCommentByAdIdAndCommentID(Integer adId, Integer commentId){
        return commentRepository.findCommentByAdPkAndCommentId(adId, commentId);
    }


}
