package com.geulkkoli.domain.comment.service;

import com.geulkkoli.domain.comment.Comments;
import com.geulkkoli.domain.comment.CommentsRepository;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.comment.dto.CommentEditDTO;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CommentsServiceTest {

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void tearDown() {
        commentsRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @DisplayName("댓글 작성 테스트")
    @Test
    void writeComment() {
        User user = User.builder()
                .email("test121@gmail.com")
                .userName("name121")
                .nickName("nickName")
                .phoneNo("010-1111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .nickName(user.getNickName())
                .title("title")
                .user(user)
                .postBody("test")
                .build();
        postRepository.save(post);

        CommentBodyDTO commentBodyDTO = CommentBodyDTO.builder()
                .commentBody("test")
                .build();
        Comments comments = commentsService.writeComment(commentBodyDTO, post, user);

        assertAll(
                () -> assertThat(comments.getCommentBody()).isEqualTo("test"),
                () -> assertThat(comments.getUser()).isEqualTo(user),
                () -> assertThat(comments.getPost()).isEqualTo(post)
        );
    }

    @DisplayName("댓글 수정 테스트")
    @Test
    void editComment() {
        User user = User.builder()
                .email("test121@gmail.com")
                .userName("name121")
                .nickName("nickName")
                .phoneNo("010-1111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .nickName(user.getNickName())
                .title("title")
                .user(user)
                .postBody("test")
                .build();
        postRepository.save(post);

        CommentBodyDTO commentBodyDTO = CommentBodyDTO.builder()
                .commentBody("test")
                .build();
        Comments comments = commentsService.writeComment(commentBodyDTO, post, user);

        CommentEditDTO commentEditDTO = CommentEditDTO.builder()
                .commentId(comments.getCommentId())
                .commentBody("test2")
                .build();
        Comments editComment = commentsService.editComment(commentEditDTO, user);

        assertAll(
                () -> assertThat(editComment.getCommentBody()).isEqualTo("test2"),
                () -> assertThat(editComment.getUser()).isEqualTo(user),
                () -> assertThat(editComment.getPost()).isEqualTo(post)
        );
    }

    @DisplayName("존재하지 않는 댓글을 수정하려고 할 때 예외가 발생하는 테스트")
    @Test
    void editCommentWithNotExistComment() {
        User user = User.builder()
                .email("test1231@gmail.com")
                .userName("name1213")
                .nickName("nickName")
                .phoneNo("010-1111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user);

        assertThrows(CommentNotFoundException.class, () ->
                commentsService.editComment(CommentEditDTO.builder()
                        .commentId(1L)
                        .commentBody("test")
                        .build(), user)
        );
    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    void deleteComment() {
        User user = User.builder()
                .email("test121@gmail.com")
                .userName("name121")
                .nickName("nickName")
                .phoneNo("010-1111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .nickName(user.getNickName())
                .title("title")
                .user(user)
                .postBody("test")
                .build();
        postRepository.save(post);

        CommentBodyDTO commentBodyDTO = CommentBodyDTO.builder()
                .commentBody("test")
                .build();
        Comments comments = commentsService.writeComment(commentBodyDTO, post, user);

        commentsService.deleteComment(comments.getCommentId(), user);

        assertThat(commentsRepository.findById(comments.getCommentId())).isEmpty();
    }

    @DisplayName("존재하지 않는 댓글을 삭제하려고 할 때 예외가 발생하는 테스트")
    @Test
    void deleteCommentWithNotExistComment() {
        User user = User.builder()
                .email("test121@gmail.com")
                .userName("name121")
                .nickName("nickName")
                .phoneNo("010-1111-2222")
                .password("1234")
                .gender("Male")
                .build();
        userRepository.save(user);

        assertThrows(CommentNotFoundException.class, () ->
                commentsService.deleteComment(1L, user));
    }
}