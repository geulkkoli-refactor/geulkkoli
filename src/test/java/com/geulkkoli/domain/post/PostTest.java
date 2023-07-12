package com.geulkkoli.domain.post;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PostTest {
    @Test
    void getPost() {
        Post post = Post.builder()
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();


        assertAll(() -> assertThat(post.getTitle()).isEqualTo("테스트"),
                () -> assertThat(post.getPostBody()).isEqualTo("나나"),
                () -> assertThat(post.getNickName()).isEqualTo("나"));


    }

    @Test
    void changeTitle() {
        Post post = Post.builder()
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();

        post.changeTitle("테스트2");

        assertThat(post.getTitle()).isEqualTo("테스트2");
    }

    @Test
    void changePostBody() {

        Post post = Post.builder()
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();

        post.changePostBody("나나나");

        assertThat(post.getPostBody()).isEqualTo("나나나");
    }

    @Test
    void changeNickName() {

        Post post = Post.builder()
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();

        post.changeNickName("너");

        assertThat(post.getNickName()).isEqualTo("너");
    }

    @Test
    void changeHits() {

        Post post = Post.builder()
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();

        post.changeHits(1);

        assertThat(post.getPostHits()).isEqualTo(1);
    }

    @Test
    void addHashTag() {
        Post post = Post.builder()
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();
        HashTag category = new HashTag("test category", HashTagType.CATEGORY);
        PostHashTag postHashTag = post.addHashTag(category);

        assertAll(() -> assertThat(postHashTag.getPost()).isEqualTo(post),
                () -> assertThat(postHashTag.getHashTag()).extracting("hashTagName").isEqualTo("test category"),
                () -> assertThat(postHashTag.getHashTag()).extracting("hashTagType").isEqualTo(HashTagType.CATEGORY));
    }

    @Test
    void deletePostHashTag() {
        Post post = Post.builder()
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();
        HashTag category = new HashTag("test category", HashTagType.CATEGORY);
        HashTag category2 = new HashTag("test category2", HashTagType.CATEGORY);
        PostHashTag postHashTag = post.addHashTag(category);
        PostHashTag postHashTag2 = post.addHashTag(category2);

        PostHashTag deletePostHashTag = post.deletePostHashTag(postHashTag);

        assertAll(() -> assertThat(post.getPostHashTags()).hasSize(1),
                () -> assertThat(Objects.requireNonNull(post.getPostHashTags()).contains(postHashTag2)));
    }


    @Test
    void deleteAllPostHashTag() {
        Post post = Post.builder()
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();
        HashTag category = new HashTag("test category", HashTagType.CATEGORY);
        HashTag category2 = new HashTag("test category2", HashTagType.CATEGORY);
        HashTag category3 = new HashTag("test category3", HashTagType.CATEGORY);
        post.addHashTag(category);
        post.addHashTag(category2);
        post.addHashTag(category3);

        post.deleteAllPostHashTag();

        assertThat(post.getPostHashTags()).isEmpty();
    }
}
