package com.geulkkoli.domain.comment;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentsTest {
    @Test
    void changeComments() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        Post post = Post.builder()
                .user(user)
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();

        Comments comments = Comments.builder()
                .user(user)
                .post(post)
                .commentBody("댓글")
                .build();
        comments.changeComments("댓글test");
        assertEquals("댓글test", comments.getCommentBody());
    }
}