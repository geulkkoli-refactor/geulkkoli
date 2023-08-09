package com.geulkkoli.domain.comment;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.comment.dto.CommentEditDTO;
import com.geulkkoli.web.post.dto.PostAddDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CommentsRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    private User user;
    private Post post01, post02;

    @AfterEach
    void afterEach () {
        commentsRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @BeforeEach
    void init(){
        User save = User.builder()
                .email("test@naver.com")
                .userName("test")
                .nickName("test")
                .phoneNo("00000000000")
                .password("123")
                .gender("male").build();

        user = userRepository.save(save);
    }

    @BeforeEach
    void beforeEach () {
        PostAddDTO postAddDTO01 = PostAddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName(user.getNickName())
                .build();
        post01 = user.writePost(postAddDTO01);
        postRepository.save(post01);

        PostAddDTO postAddDTO02 = PostAddDTO.builder()
                .title("testTitle02")
                .postBody("test postbody 02")
                .nickName(user.getNickName())
                .build();
        post02 = user.writePost(postAddDTO02);
        postRepository.save(post02);
    }

    @Test
    void 댓글_저장 () {
        Comments comment = user.writeComment(CommentBodyDTO.builder()
                .commentBody("test댓글")
                .build(), post01);

        Comments save = commentsRepository.save(comment);

        assertThat(comment.getCommentBody()).isEqualTo(save.getCommentBody());
        assertThat("test댓글").isEqualTo(save.getCommentBody());
    }

    @Test
    void 게시물_댓글_저장 () {
        Comments comment = user.writeComment(CommentBodyDTO.builder()
                .commentBody("test댓글")
                .build(), post01);
        Comments save = commentsRepository.save(comment);

        Comments comment2 = user.writeComment(CommentBodyDTO.builder()
                .commentBody("test댓글2")
                .build(), post01);
        Comments save2 = commentsRepository.save(comment2);

        ArrayList<Comments> postComment = new ArrayList<>(post01.getComments());
        assertThat(comment.getCommentBody()).isEqualTo(save.getCommentBody());
        assertThat("test댓글").isEqualTo(save.getCommentBody());
        assertThat(postComment.contains(save)).isEqualTo(true);

    }

    @Test
    void findById () {
        Comments comment = user.writeComment(CommentBodyDTO.builder()
                .commentBody("test댓글")
                .build(), post01);
        Comments save = commentsRepository.save(comment);
        Comments find = commentsRepository.findById(save.getCommentId())
                .orElseThrow(() -> new NoSuchElementException("no comment id found : " + save.getCommentId()));

        assertThat(find).isEqualTo(save);
        assertThat(find.getCommentBody()).isEqualTo("test댓글");
        assertThat(find.getPost()).isEqualTo(save.getPost());
        assertThat(find.getUser()).isEqualTo(save.getUser());
    }


    @Test
    void 전체_리스트_조회() {
        Comments comment = user.writeComment(CommentBodyDTO.builder()
                .commentBody("test댓글")
                .build(), post01);
        Comments save = commentsRepository.save(comment);
        List<Comments> all = commentsRepository.findAll();
        assertThat(1).isEqualTo(all.size());
        assertThat("test댓글").isEqualTo(all.get(0).getCommentBody());
    }

    @Test
    void 수정() {
        Comments comment = user.writeComment(CommentBodyDTO.builder()
                .commentBody("test댓글")
                .build(), post01);
        Comments saveComment = commentsRepository.save(comment);
        CommentEditDTO updateParam = new CommentEditDTO(saveComment.getCommentId(), "수정바디");
        Comments updateComment = user.editComment(saveComment, updateParam);
        commentsRepository.save(updateComment);

        Comments find = commentsRepository.findById(saveComment.getCommentId())
                .orElseThrow(() -> new NoSuchElementException("no comment id found : " + saveComment.getCommentId()));

        assertThat(find.getCommentBody()).isEqualTo(saveComment.getCommentBody());
        assertThat(find.getCommentBody()).isEqualTo("수정바디");
    }

    @Test
    void 삭제() {
        Comments comment = user.writeComment(CommentBodyDTO.builder()
                .commentBody("test댓글")
                .build(), post01);
        Comments save = commentsRepository.save(comment);

        Comments comment2 = user.writeComment(CommentBodyDTO.builder()
                .commentBody("test댓글2")
                .build(), post02);
        Comments save2 = commentsRepository.save(comment2);

        user.deleteComment(save);
        commentsRepository.delete(save);

        List<Comments> all = user.getComments();

        assertAll(
                () -> assertThat(all).hasSize(1),
                () -> assertThat(all).doesNotContain(save),
                () -> assertThat(all).contains(save2)
        );
    }
}