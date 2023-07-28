package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.time.Instant;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    AdService adService;
    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    AuthService authService;

    @BeforeEach
    public void setUp() {
        authService.register(new RegisterReq("user@gmail.com", "password", "Ivan",
                "Petrov", "+7900000000", Role.USER), Role.USER);
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void commentToCommentDTOreturnCorrectDTO()  {
        User author = userService.getAuthUser();
        Ad ad = new Ad(author, new Image("imageName"), 1, "descr", 100, "title");
        Comment comment = new Comment(ad,author,Instant.now().toEpochMilli(),"text");
        CommentDTO commentDto = commentService.commentToCommentDTO(comment);

        Assertions.assertEquals(comment.getAuthor().getId(), commentDto.getAuthor());
        Assertions.assertEquals(comment.getAuthor().getFirstName(), commentDto.getAuthorFirstName());
        Assertions.assertEquals(comment.getCommentId(),commentDto.getPk());
        Assertions.assertEquals(comment.getText(),commentDto.getText());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void getAllCommentReturnsAllExistsComments() {
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "descr", savedImage,
                100, "title"));
        commentService.addCommentToAd(ad.getPk(), new CreateOrUpdateComment("textComment"));
        Assertions.assertEquals(1, commentRepository.findCommentsByAd(ad).size());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void getAllCommentReturnsEmptyListIfThereIsNoThisComments() {
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "descr", savedImage,
            100, "title"));
        int actual = commentRepository.findCommentsByAd(ad).size();
        Assertions.assertEquals(0, actual);
}

    @Test
    @WithMockUser("user@gmail.com")
    public void removeCommentIfUserIsAuthor() {
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "descr", savedImage,
                100, "title"));
        commentService.addCommentToAd(ad.getPk(), new CreateOrUpdateComment("textComment"));
        commentService.deleteCommentById(ad.getPk(),1);
        int actual = commentRepository.findCommentsByAd(ad).size();
        Assertions.assertEquals(0, actual);
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void returnCorrectCommentAfterUpdateCommentById(){
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "descr", savedImage,
                100, "title"));
        commentService.addCommentToAd(ad.getPk(), new CreateOrUpdateComment("textComment"));
        Comment newComment = commentRepository.findCommentByCommentId(1);
        String expected = "newCommentText";
        newComment.setText(expected);
        commentRepository.save(newComment);
        Assertions.assertEquals(expected, commentRepository.findCommentByCommentId(1).getText());
    }

    @Test
    @WithMockUser("user@gmail.com")
    public void getCorrectCommentByAdIdAndCommentID(){
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "descr", savedImage,
                100, "title"));
        commentService.addCommentToAd(ad.getPk(), new CreateOrUpdateComment("textComment"));
        Comment actual = commentRepository.findCommentByAdPkAndCommentId(ad.getPk(),1);
        Comment expected = new Comment(ad,author,Instant.now().toEpochMilli(),"textComment");

        Assertions.assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(expected.getAuthor().getFirstName(), actual.getAuthor().getFirstName());
        Assertions.assertEquals(expected.getText(),actual.getText());
    }

}

