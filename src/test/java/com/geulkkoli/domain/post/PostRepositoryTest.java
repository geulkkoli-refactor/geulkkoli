package com.geulkkoli.domain.post;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.EditDTO;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PostRepositoryTest {

    /**
     * 단위 테스트 구현을 위한 구현체
     */
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private PostHashTagRepository postHashTagRepository;

    private User user;

    @AfterEach
    void afterEach() {
        postRepository.deleteAllInBatch();
    }

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
        AddDTO addDTO = AddDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .nickName("점심뭐먹지").build();

        Post post = user.writePost(addDTO);
        postRepository.save(post);


        AddDTO addDTO1 = AddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName("점심뭐먹지").build();
        Post post1 = user.writePost(addDTO1);

        postRepository.save(post1);

        AddDTO addDTO2 = AddDTO.builder()
                .title("testTitle02")
                .postBody("test postbody 02")
                .nickName("점심뭐먹지").build();

        Post post2 = user.writePost(addDTO2);

        postRepository.save(post2);

        AddDTO addDTO3 = AddDTO.builder()
                .title("testTitle03")
                .postBody("test postbody 03")
                .nickName("점심뭐먹지").build();

        postRepository.save(user.writePost(addDTO3));
    }

    @Test
    void save() {
        Post post = user.writePost(AddDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .nickName("점심뭐먹지").build());
        Post save = postRepository.save(post);
        assertThat(save.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void findById() {
        Post post = user.writePost(AddDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .nickName("점심뭐먹지").build());

        Post save = postRepository.save(post);

        Post find = postRepository.findById(save.getPostId())
                .orElseThrow(() -> new NoSuchElementException("No post found id matches : " + save.getPostId()));

        assertThat(save).isEqualTo(find);
        log.info("findDate={}", find.getUpdatedAt());
    }

    @Test
    void findAll() {
        List<Post> all = postRepository.findAll();
        assertThat(4).isEqualTo(all.size());
    }


    @Test
    void update() {
        Post post = user.writePost(AddDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .nickName("점심뭐먹지").build());
        Post savePost = postRepository.save(post);

        Post modifyPost = user.editPost(savePost, new EditDTO(savePost.getPostId(), "modifyTitle", "modifyBody", savePost.getNickName(), "수정 test"));

        postRepository.save(modifyPost);

        Post one = postRepository.findById(savePost.getPostId())
                .orElseThrow(() -> new NoSuchElementException("No post found id matches : " + savePost.getPostId()));
        assertThat(one.getTitle()).isEqualTo(modifyPost.getTitle());
        assertThat(one.getPostBody()).isEqualTo(modifyPost.getPostBody());
    }

    @Test
    void delete() {
        Post post = user.writePost(AddDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .nickName("점심뭐먹지").build());
        Post savePost = postRepository.save(post);
        Post deletedPost = user.deletePost(savePost);
        postRepository.delete(deletedPost);
        List<Post> all = postRepository.findAll();

        assertThat(all.size()).isEqualTo(4);
    }

    @Test
    void 유저_삭제() throws Exception {
        //given
        userRepository.delete(user);

        //when
        List<Post> all = postRepository.findAll();
        List<User> userAll = userRepository.findAll();

        //then
        log.info("allSize={}", all.size());
        log.info("userAll={}", userAll.size());
        for (Post post : all) {
            log.info("post={}", post);
        }
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    void allPostsMultiHashTags() {
        AddDTO addDTO01 = AddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName(user.getNickName())
                .build();
        Post post01 = user.writePost(addDTO01);
        postRepository.save(post01);

        AddDTO addDTO02 = AddDTO.builder()
                .title("testTitle02")
                .postBody("test postbody 02")
                .nickName(user.getNickName())
                .build();

        AddDTO addDTO03 = AddDTO.builder()
                .title("testTitle03")
                .postBody("test postbody 03")
                .nickName(user.getNickName())
                .build();

        AddDTO addDTO4 = AddDTO.builder()
                .title("testTitle04")
                .postBody("test postbody 04")
                .nickName(user.getNickName())
                .build();

        AddDTO addDTO5 = AddDTO.builder()
                .title("testTitle05")
                .postBody("test postbody 05")
                .nickName(user.getNickName())
                .build();

        Post post02 = user.writePost(addDTO02);
        Post post03 = user.writePost(addDTO03);
        Post post04 = user.writePost(addDTO4);
        Post post05 = user.writePost(addDTO5);

        postRepository.save(post02);
        postRepository.save(post03);
        postRepository.save(post04);
        postRepository.save(post05);

        HashTag tag1 = hashTagRepository.save(new HashTag("일반글", HashTagType.GENERAL));
        HashTag tag2 = hashTagRepository.save(new HashTag("공지글", HashTagType.MANAGEMENT));
        HashTag tag3 = hashTagRepository.save(new HashTag("판타지", HashTagType.GENERAL));
        HashTag tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.GENERAL));

        PostHashTag save1 = post01.addHashTag(tag1);
        PostHashTag save2 = post01.addHashTag(tag3);

        PostHashTag save3 = post02.addHashTag(tag1);
        PostHashTag save4 = post02.addHashTag(tag4);

        PostHashTag save5 = post03.addHashTag(tag1);
        PostHashTag save6 = post03.addHashTag(tag3);

        PostHashTag save7 = post04.addHashTag(tag1);
        PostHashTag save8 = post04.addHashTag(tag3);

        postHashTagRepository.save(save1);
        postHashTagRepository.save(save2);
        postHashTagRepository.save(save3);
        postHashTagRepository.save(save4);
        postHashTagRepository.save(save5);
        postHashTagRepository.save(save6);
        postHashTagRepository.save(save7);
        postHashTagRepository.save(save8);


        List<String> hashTagNames = List.of(tag1.getHashTagName(), tag3.getHashTagName());

        List<Post> posts = postRepository.allPostsMultiHashTags(hashTagNames);

        log.info("posts={}", posts);

        assertThat(posts).hasSize(3);
        assertThat(posts).contains(post01, post03, post04);
    }

    @DisplayName("제목과 멀티 해시태그가 일치하는 게시글 조회")
    @Test
    void allPostTitleAndMultiHashTags(){
        AddDTO addDTO01 = AddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName(user.getNickName())
                .build();
        Post post01 = user.writePost(addDTO01);
        postRepository.save(post01);

        AddDTO addDTO02 = AddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 02")
                .nickName(user.getNickName())
                .build();

        AddDTO addDTO03 = AddDTO.builder()
                .title("testTitle03")
                .postBody("test postbody 03")
                .nickName(user.getNickName())
                .build();

        AddDTO addDTO4 = AddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 04")
                .nickName(user.getNickName())
                .build();

        AddDTO addDTO5 = AddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 05")
                .nickName(user.getNickName())
                .build();

        Post post02 = user.writePost(addDTO02);
        Post post03 = user.writePost(addDTO03);
        Post post04 = user.writePost(addDTO4);
        Post post05 = user.writePost(addDTO5);

        postRepository.save(post02);
        postRepository.save(post03);
        postRepository.save(post04);
        postRepository.save(post05);

        HashTag tag1 = hashTagRepository.save(new HashTag("일반글", HashTagType.GENERAL));
        HashTag tag2 = hashTagRepository.save(new HashTag("공지글", HashTagType.MANAGEMENT));
        HashTag tag3 = hashTagRepository.save(new HashTag("판타지", HashTagType.GENERAL));
        HashTag tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.GENERAL));

        PostHashTag save1 = post01.addHashTag(tag1);
        PostHashTag save2 = post01.addHashTag(tag3);

        PostHashTag save3 = post02.addHashTag(tag1);
        PostHashTag save4 = post02.addHashTag(tag4);

        PostHashTag save5 = post03.addHashTag(tag1);
        PostHashTag save6 = post03.addHashTag(tag3);

        PostHashTag save7 = post04.addHashTag(tag1);
        PostHashTag save8 = post04.addHashTag(tag3);

        PostHashTag save9 = post05.addHashTag(tag1);
        PostHashTag save10 = post05.addHashTag(tag3);
        PostHashTag save11 = post05.addHashTag(tag4);

        postHashTagRepository.save(save1);
        postHashTagRepository.save(save2);
        postHashTagRepository.save(save3);
        postHashTagRepository.save(save4);
        postHashTagRepository.save(save5);
        postHashTagRepository.save(save6);
        postHashTagRepository.save(save7);
        postHashTagRepository.save(save8);
        postHashTagRepository.save(save9);
        postHashTagRepository.save(save10);
        postHashTagRepository.save(save11);


        List<String> hashTagNames = List.of(tag1.getHashTagName(), tag3.getHashTagName());

        List<Post> posts = postRepository.allPostsTitleAndMultiPosts("testTitle01", hashTagNames);
        List<Post> posts02 = postRepository.allPostsTitleAndMultiPosts("test", hashTagNames);

        log.info("posts={}", posts);

        assertThat(posts).hasSize(3);
        assertThat(posts02).hasSize(4);
        assertThat(posts).contains(post01, post04, post05);
        assertThat(posts02).contains(post01, post03, post04, post05);
    }

}