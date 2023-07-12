package com.geulkkoli.domain.posthashtag;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@Transactional
@ActiveProfiles("test")
class PostHashTagRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private PostHashTagRepository postHashTagRepository;

    private User user;
    private Post post01, post02;
    private HashTag tag1;
    private HashTag tag3;
    private HashTag tag2;
    private HashTag tag4;

    @AfterEach
    void afterEach() {
        postHashTagRepository.deleteAll();
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

        tag1 = hashTagRepository.save(new HashTag("일반글", HashTagType.GENERAL));
        tag2 = hashTagRepository.save(new HashTag("공지글", HashTagType.MANAGEMENT));
        tag3 = hashTagRepository.save(new HashTag("판타지", HashTagType.CATEGORY));
        tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.CATEGORY));
    }

    @Test
    void 게시글_해시태그_저장() throws Exception {
        //given
        PostHashTag save = post01.addHashTag(tag1);

        //when
        PostHashTag postHashTag = postHashTagRepository.save(save);

        //then
        assertThat(postHashTag.getHashTag().getHashTagName()).isEqualTo("일반글");
    }

    @Test
    @Transactional
    void 게시글_다중_해시태그_저장() throws Exception {
        //given
        PostHashTag save1 = post01.addHashTag(tag1);
        PostHashTag save2 = post01.addHashTag(tag3);
        PostHashTag save3 = post01.addHashTag(tag4);

        //when
        PostHashTag postHashTag1 = postHashTagRepository.save(save1);
        PostHashTag postHashTag2 = postHashTagRepository.save(save2);
        PostHashTag postHashTag3 = postHashTagRepository.save(save3);


        //then
        assertThat(post01.getPostHashTags()).hasSize(3);
        assertThat(post01.getPostHashTags()).contains(postHashTag1);
        assertThat(post01.getPostHashTags()).contains(postHashTag2);
        assertThat(post01.getPostHashTags()).contains(postHashTag3);
    }

    @Test
    void 게시글_해시태그_불러오기() throws Exception {
        //given
        PostHashTag postHashTag1 = postHashTagRepository.save(post01.addHashTag(tag1));
        PostHashTag postHashTag2 = postHashTagRepository.save(post01.addHashTag(tag3));
        PostHashTag postHashTag3 = postHashTagRepository.save(post01.addHashTag(tag4));
        //when
        List<PostHashTag> list = post01.getPostHashTags();

        //then
        assertThat(list.size()).isEqualTo(3);
        assertThat(list.get(1).getHashTag().getHashTagName()).isEqualTo("판타지");
    }

    @Test
    void 해시태그로_게시글_전부_찾기() throws Exception {
        //given
        PostHashTag save01 = postHashTagRepository.save(post01.addHashTag(tag1));
        PostHashTag save02 = postHashTagRepository.save(post02.addHashTag(tag1));

        //when
        List<PostHashTag> postHashTagList = postHashTagRepository.findAllByHashTag(tag1);
        //then
        assertThat(postHashTagList.size()).isEqualTo(2);
        assertThat(postHashTagList.get(0).getHashTag().getHashTagName()).isEqualTo("일반글");
    }

}