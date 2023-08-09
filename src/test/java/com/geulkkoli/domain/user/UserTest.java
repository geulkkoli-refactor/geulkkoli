package com.geulkkoli.domain.user;

import com.geulkkoli.application.security.LockExpiredTimeException;
import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.security.RoleEntity;
import com.geulkkoli.domain.admin.Report;
import com.geulkkoli.domain.comment.Comments;
import com.geulkkoli.domain.favorites.Favorites;
import com.geulkkoli.domain.follow.Follow;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.comment.dto.CommentEditDTO;
import com.geulkkoli.web.post.dto.PostAddDTO;
import com.geulkkoli.web.post.dto.PostEditRequestDTO;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    @Test
    @DisplayName("계정 잠금 테스트")
    void isLock() {

        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        user.lock("reason", LocalDateTime.now().plusDays(7));

        assertTrue(user.isLock());
    }

    @Test
    @DisplayName("계정 잠금 시간이 설정이 되어있지 않다면 LockExpiredTimeException을 던진다")
    void if_no_lock_expiration_time_is_throw_LockedExpiredTimeException() {

        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();
        user.lock("reason", null);

        assertThrows(LockExpiredTimeException.class, user::isLock);
    }

    @Test
    @DisplayName("신고글 작성")
    void writeReport() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();
        Post post = Post.builder()
                .nickName("nickName")
                .title("title")
                .postBody("content")
                .build();

        Report report = user.writeReport(post, "reason");

        assertThat(user).has(new Condition<>(u -> u.getReports().contains(report), "report가 추가되었다"));
    }

    @DisplayName("report를 삭제한다")
    @Test
    void deleteReport() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();
        Post post = Post.builder()
                .nickName("nickName")
                .title("title")
                .postBody("content")
                .build();

        Post post1 = Post.builder()
                .nickName("nickName1")
                .title("title1")
                .postBody("content1")
                .build();

        Report report = user.writeReport(post, "reason");
        Report report1 = user.writeReport(post1, "reason1");

        user.deleteReport(report1);

        assertThat(user).has(new Condition<>(u -> !u.getReports().contains(report1), "report가 삭제되었다"));
    }

    @DisplayName("역할을 추가한다")
    @Test
    void addRole() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();
        RoleEntity roleEntity = user.addRole(Role.USER);

        assertThat(roleEntity).isNotNull().has(new Condition<>(r -> r.getRole() == Role.USER, "USER 역할이 추가되었다")).has(new Condition<>(r -> r.getUsers().contains(user), "user가 추가되었다"));
    }

    @DisplayName("글을 작성한다")
    @Test
    void writePost() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();


        PostAddDTO postAddDTO = PostAddDTO.builder()
                .nickName(user.getNickName())
                .authorId(1L)
                .title("title")
                .postBody("content")
                .build();
        Post post = user.writePost(postAddDTO);

        assertAll(
                () -> assertThat(user).has(new Condition<>(u -> u.getPosts().contains(post), "post가 추가되었다")),
                () -> assertThat(post).has(new Condition<>(p -> p.getUser().equals(user), "user가 추가되었다"))
        );
    }

    @DisplayName("글을 수정한다")
    @Test
    void editPost() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();


        PostAddDTO postAddDTO = PostAddDTO.builder()
                .nickName(user.getNickName())
                .authorId(1L)
                .title("title")
                .postBody("content")
                .build();
        Post post = user.writePost(postAddDTO);

        PostEditRequestDTO postEditRequestDTO = PostEditRequestDTO.builder()
                .postId(1L)
                .title("title1")
                .postBody("content1")
                .build();

        Post post1 = user.editPost(post, postEditRequestDTO);

        assertAll(
                () -> assertThat(post1).has(new Condition<>(p -> p.getTitle().equals("title1"), "title이 수정되었다")),
                () -> assertThat(post1).has(new Condition<>(p -> p.getPostBody().equals("content1"), "content가 수정되었다"))
        );
    }

    @DisplayName("글을 삭제한다")
    @Test
    void deletePost() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();


        PostAddDTO postAddDTO = PostAddDTO.builder()
                .nickName(user.getNickName())
                .authorId(1L)
                .title("title")
                .postBody("content")
                .build();
        Post post = user.writePost(postAddDTO);
        Post post2 = user.writePost(postAddDTO);
        Post post1 = user.deletePost(post);

        assertThat(user).has(new Condition<>(u -> !u.getPosts().contains(post1), "post가 삭제되었다"));
    }

    @DisplayName("좋아요를 누른다")
    @Test
    void pressFavorite() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        User user1 = User.builder()
                .email("email1@gmail.com")
                .userName("userName1")
                .password("password1")
                .nickName("nickName1")
                .phoneNo("0102221112")
                .gender("male")
                .build();

        Post post = user1.writePost(PostAddDTO.builder()
                .nickName(user1.getNickName())
                .authorId(1L)
                .title("title")
                .postBody("content")
                .build());
        Favorites favorites = user.pressFavorite(post);

        assertThat(favorites).has(new Condition<>(f -> f.getUser().equals(user), "user가 추가되었다"))
                .has(new Condition<>(f -> f.getPost().equals(post), "post가 추가되었다"));
    }

    @DisplayName("구독한다")
    @Test
    void follow() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();
        User user1 = User.builder()
                .email("email1@gmail.com")
                .userName("userName1")
                .password("password1")
                .nickName("nickName1")
                .phoneNo("0102221112")
                .gender("male")
                .build();

        Follow follow = user.follow(user1);

        assertThat(follow).has(new Condition<>(f -> f.getFollower().equals(user), "follower가 추가되었다"))
                .has(new Condition<>(f -> f.getFollowee().equals(user1), "following이 추가되었다"));
    }

    @DisplayName("구독을 취소한다")
    @Test
    void unfollow() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        User user1 = User.builder()
                .email("email1@gmail.com")
                .userName("userName1")
                .password("password1")
                .nickName("nickName1")
                .phoneNo("0102221112")
                .gender("male")
                .build();

        Follow follow = user.follow(user1);
        user.unfollow(follow);

        assertThat(user).has(new Condition<>(u -> !u.getFollowees().contains(follow), "follow가 삭제되었다"));
    }

    @Test
    void writeComment() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        User user1 = User.builder()
                .email("email1@gmail.com")
                .userName("userName1")
                .password("password1")
                .nickName("nickName1")
                .phoneNo("0102221112")
                .gender("male")
                .build();

        Post post = user1.writePost(PostAddDTO.builder()
                .nickName(user1.getNickName())
                .authorId(1L)
                .title("title")
                .postBody("content")
                .build());

        CommentBodyDTO commentBody = new CommentBodyDTO("commentBody");
        Comments comments = user.writeComment(commentBody, post);

        assertThat(comments).has(new Condition<>(c -> c.getUser().equals(user), "user가 추가되었다"))
                .has(new Condition<>(c -> c.getPost().equals(post), "post가 추가되었다"))
                .has(new Condition<>(c -> c.getCommentBody().equals(commentBody.getCommentBody()), "commentBody가 추가되었다"));
    }

    @Test
    void editComment() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        User user1 = User.builder()
                .email("email1@gmail.com")
                .userName("userName1")
                .password("password1")
                .nickName("nickName1")
                .phoneNo("0102221112")
                .gender("male")
                .build();

        Post post = user1.writePost(PostAddDTO.builder()
                .nickName(user1.getNickName())
                .authorId(1L)
                .title("title")
                .postBody("content")
                .build());

        CommentBodyDTO commentBody = new CommentBodyDTO("commentBody");
        Comments comments = user.writeComment(commentBody, post);
        CommentEditDTO commentEditDTO = new CommentEditDTO(1L, "commentBody1");
        Comments comments1 = user.editComment(comments, commentEditDTO);

        assertThat(comments1).has(new Condition<>(c -> c.getCommentBody().equals(commentEditDTO.getCommentBody()), "commentBody가 수정되었다"));
    }

    @DisplayName("댓글을 삭제한다")
    @Test
    void deleteComment() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        User user1 = User.builder()
                .email("email1@gmail.com")
                .userName("userName1")
                .password("password1")
                .nickName("nickName1")
                .phoneNo("0102221112")
                .gender("male")
                .build();

        Post post = user1.writePost(PostAddDTO.builder()
                .nickName(user1.getNickName())
                .authorId(1L)
                .title("title")
                .postBody("content")
                .build());

        CommentBodyDTO commentBody = new CommentBodyDTO("commentBody");
        Comments comments = user.writeComment(commentBody, post);
        user.deleteComment(comments);

        assertThat(user).has(new Condition<>(u -> !u.getComments().contains(comments), "comment가 삭제되었다"));
    }


    @DisplayName("좋아요를 취소한다")
    @Test
    void cancelFavorite() {

        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        User user1 = User.builder()
                .email("email1@gmail.com")
                .userName("userName1")
                .password("password1")
                .nickName("nickName1")
                .phoneNo("0102221112")
                .gender("male")
                .build();

        Post post = user1.writePost(PostAddDTO.builder()
                .nickName(user1.getNickName())
                .authorId(1L)
                .title("title")
                .postBody("content")
                .build());
        Favorites favorites = user.pressFavorite(post);
        user.cancelFavorite(favorites);

        assertThat(user).has(new Condition<>(u -> !u.getFavorites().contains(favorites), "favorite가 삭제되었다"));
    }
}