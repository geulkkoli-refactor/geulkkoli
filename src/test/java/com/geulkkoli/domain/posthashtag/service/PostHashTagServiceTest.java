package com.geulkkoli.domain.posthashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.EditDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;

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
    @Autowired
    private PostService postService;

    private User user;
    private Post post, post01, post02, post03;
    private HashTag tag1, notice, fantasy, tag4, tag5, tag6, tag7, tag8, fangaia;

    List<Post> posts;

    @BeforeEach
    void init() {
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
    void beforeEach() {
        for (int i = 0; i < 10; i++) {
            AddDTO addDTO = AddDTO.builder()
                    .title("testTitle" + i)
                    .postBody("test postbody " + i)
                    .nickName(user.getNickName())
                    .build();
            post = user.writePost(addDTO);
            postRepository.save(post);
        }


        AddDTO addDTO01 = AddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName(user.getNickName())
                .build();
        post01 = user.writePost(addDTO01);
        postRepository.save(post01);

        AddDTO addDTO02 = AddDTO.builder()
                .title("testTitle02")
                .postBody("test postbody 02")
                .nickName(user.getNickName())
                .build();
        post02 = user.writePost(addDTO02);
        postRepository.save(post02);

        AddDTO addDTO03 = AddDTO.builder()
                .title("testTitle03")
                .postBody("test postbody 03")
                .nickName(user.getNickName())
                .build();
        post03 = user.writePost(addDTO03);
        postRepository.save(post03);

        posts = postRepository.findAll();

        tag1 = hashTagRepository.save(new HashTag("일반글", HashTagType.GENERAL));
        notice = hashTagRepository.save(new HashTag("공지글", HashTagType.MANAGEMENT));
        fantasy = hashTagRepository.save(new HashTag("판타지", HashTagType.GENERAL));
        tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.GENERAL));
        tag5 = hashTagRepository.save(new HashTag("단편소설", HashTagType.GENERAL));
        tag6 = hashTagRepository.save(new HashTag("시", HashTagType.GENERAL));
        tag7 = hashTagRepository.save(new HashTag("이상", HashTagType.GENERAL));
        tag8 = hashTagRepository.save(new HashTag("게임", HashTagType.GENERAL));
        fangaia = hashTagRepository.save(new HashTag("판게아", HashTagType.GENERAL));

        hashTagRepository.save(new HashTag("소설", HashTagType.CATEGORY));
        hashTagRepository.save(new HashTag("완결", HashTagType.STATUS));
    }


    @DisplayName("글 작성 후 해시태그를 추가할 수 있다.")
    @Test
    void addHashTagsToPost() {
        AddDTO addDTO = AddDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagListString("판게아")
                .tagCategory("소설")
                .tagStatus("완결")
                .nickName("test")
                .authorId(1L)
                .build();

        Post post1 = user.writePost(addDTO);
        postRepository.save(post1);

        Post post = postHashTagService.addHashTagsToPost(post1, addDTO);

        assertThat(post.getPostHashTags()).hasSize(3);

    }

    @DisplayName("글 작성 후 해시태그를 바꿀 수 있다.")
    @Test
    void editHashTagsToPost() {
        AddDTO addDTO = AddDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagListString("신과 함께")
                .tagCategory("소설")
                .tagStatus("완결")
                .nickName("test")
                .authorId(1L)
                .build();

        Post post1 = user.writePost(addDTO);
        postRepository.save(post1);
        Post post = postHashTagService.addHashTagsToPost(post1, addDTO);

        EditDTO editDTO = EditDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagListString("판타지")
                .tagCategory("소설")
                .tagStatus("완결")
                .nickName("test")
                .postId(post.getPostId())
                .build();

        Post editPost = postHashTagService.editHashTagsToPost(post, editDTO);

        assertThat(editPost.getPostHashTags()).hasSize(3);
        assertThat(editPost.getPostHashTags()).have(new Condition<>(postHashTag -> postHashTag.getHashTag().getHashTagName().contains(fantasy.getHashTagName()), "판타지"));
    }

    @DisplayName("공지글 작성 후 해시태그를 추가할 수 있다.")
    @Test
    void addHashTagsToPostNotice() {
        AddDTO addDTO = AddDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagListString("판타지")
                .tagCategory("소설")
                .tagStatus("완결")
                .nickName("test")
                .authorId(1L)
                .build();

        Post post1 = user.writePost(addDTO);
        postRepository.save(post1);
        Post post = postHashTagService.addHashTagsToPostNotice(post1, addDTO);

        assertThat(post.getPostHashTags()).hasSize(4);
        assertThat(post.getPostHashTags().get(0)).has(new Condition<>(postHashTag -> postHashTag.getHashTag().getHashTagName().contains(notice.getHashTagName()), "공지글"));
    }

    @Test
    void editHashTagsToPostNotice() {
        AddDTO addDTO = AddDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagListString("판타지")
                .tagCategory("소설")
                .tagStatus("완결")
                .nickName("test")
                .authorId(1L)
                .build();

        Post post1 = user.writePost(addDTO);
        postRepository.save(post1);
        Post post = postHashTagService.addHashTagsToPostNotice(post1, addDTO);

        EditDTO editDTO = EditDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagListString("판게아")
                .tagCategory("소설")
                .tagStatus("완결")
                .nickName("test")
                .postId(post.getPostId())
                .build();

        Post editPost = postHashTagService.editHashTagsToPostNotice(post, editDTO);

        assertThat(editPost.getPostHashTags()).hasSize(4);
        assertThat(editPost.getPostHashTags().get(1)).has(new Condition<>(postHashTag -> postHashTag.getHashTag().getHashTagName().contains(fangaia.getHashTagName()), "판게아"));
    }
}