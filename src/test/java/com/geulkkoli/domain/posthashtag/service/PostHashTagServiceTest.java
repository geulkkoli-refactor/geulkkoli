package com.geulkkoli.domain.posthashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.blog.dto.WriteRequestDTO;
import com.geulkkoli.web.blog.dto.ArticleEditRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PostHashTagServiceTest {

    @Autowired
    private PostHashTagService postHashTagService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HashTagRepository hashTagRepository;



    private User user;
    private HashTag notice, fantasy, fangaia;


    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        hashTagRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
    @BeforeEach
    void init() {
        User save = User.builder()
                .email("test" + UUID.randomUUID() + "@naver.com")
                .userName("test")
                .nickName("test" +  UUID.randomUUID())
                .phoneNo("12400608000")
                .password("123")
                .gender("male").build();

        user = userRepository.save(save);
    }

    @BeforeEach
    void beforeEach() {

        notice = hashTagRepository.save(new HashTag("공지글", HashTagType.GENERAL));
        fantasy = hashTagRepository.save(new HashTag("판타지", HashTagType.GENERAL));
        fangaia = hashTagRepository.save(new HashTag("판게아", HashTagType.GENERAL));

        hashTagRepository.save(new HashTag("소설", HashTagType.GENERAL));
        hashTagRepository.save(new HashTag("완결", HashTagType.STATUS));

    }


    @DisplayName("글 작성 후 해시태그를 추가할 수 있다.")
    @Test
    void addHashTagsToPost() {
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagList("#해시태그#소설#완결")
                .nickName("test")
                .authorId(1L)
                .build();

        Post post1 = user.writePost(writeRequestDTO);
        postRepository.save(post1);

        Post post = postHashTagService.addHashTagsToPost(post1, writeRequestDTO);

        assertThat(post.getPostHashTags()).hasSize(3);
        assertThat(post.getPostHashTags().get(post.getPostHashTags().size() - 1)).has(new Condition<>(postHashTag -> postHashTag.getHashTag().getHashTagName().contains("해시태그"), "새로운 해시태그"));

    }

    @DisplayName("글 작성 후 빈 해시태그가 들어올 시 아무것도 추가하지 않는다.")
    @Test
    void addHashTagsToPost_blank() {
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagList("")
                .nickName("test")
                .authorId(1L)
                .build();

        Post post1 = user.writePost(writeRequestDTO);
        postRepository.save(post1);

        Post post = postHashTagService.addHashTagsToPost(post1, writeRequestDTO);

        assertThat(post.getPostHashTags()).isEmpty();
    }

    @DisplayName("글 작성 후 해시태그를 바꿀 수 있다.")
    @Test
    void editHashTagsToPost() {
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagList("#신과함께#소설#완결")
                .nickName("test")
                .authorId(1L)
                .build();

        Post post1 = user.writePost(writeRequestDTO);
        postRepository.save(post1);
        Post post = postHashTagService.addHashTagsToPost(post1, writeRequestDTO);

        ArticleEditRequestDTO articleEditRequestDTO = ArticleEditRequestDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tags("완결")
                .nickName("test")
                .postId(post.getPostId())
                .build();

        Post editPost = postHashTagService.editHashTagsToPost(post, articleEditRequestDTO);

        assertThat(editPost.getPostHashTags()).hasSize(1);
        assertThat(editPost.getPostHashTags().get(0)).has(new Condition<>(postHashTag -> postHashTag.getHashTag().getHashTagName().contains("완결"), "판타지"));
    }

}