package com.geulkkoli.domain.posthashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.hashtag.service.HashTagFindService;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.PostRequestListDTO;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class PostHahTagFindServiceTest {
    @Autowired
    private PostHahTagFindService postHahTagFindService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostHashTagRepository postHashTagRepository;
    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private HashTagFindService hashTagFindService;
    @Autowired
    private PostService postService;

    private User user;
    private Post post, post01, post02, post03;
    private HashTag tag1, notice, tag3, tag4, tag5, tag6, tag7, tag8, tag9;

    List<Post> posts;

    @AfterEach
    void tearDown() {
        postHashTagRepository.deleteAllInBatch();
        hashTagRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
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
        tag3 = hashTagRepository.save(new HashTag("판타지", HashTagType.GENERAL));
        tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.GENERAL));
        tag5 = hashTagRepository.save(new HashTag("단편소설", HashTagType.GENERAL));
        tag6 = hashTagRepository.save(new HashTag("시", HashTagType.GENERAL));
        tag7 = hashTagRepository.save(new HashTag("이상", HashTagType.GENERAL));
        tag8 = hashTagRepository.save(new HashTag("게임", HashTagType.GENERAL));
        tag9 = hashTagRepository.save(new HashTag("판게아", HashTagType.GENERAL));

        hashTagRepository.save(new HashTag("소설", HashTagType.CATEGORY));
        hashTagRepository.save(new HashTag("완결", HashTagType.STATUS));
    }

    @Test
    @DisplayName("실제로 검색 타입, 검색어에 따라 잘 찾을 수 있는지")
    public void searchPostsListByHashTagVer() {
        //given
        AddDTO addDTO = AddDTO.builder()
                .title("test01")
                .postBody("TestingCode01")
                .tagList("신과 함께 소설 완결")
                .nickName("test")
                .authorId(1L)
                .build();

        for (int i = 0; i < 10; i++) {
            postService.savePost(addDTO, user);
        }

        String searchWords = "소설#완결#신과 함께";
        List<HashTag> hashTag = hashTagFindService.findHashTag(searchWords);

        //when
        Pageable pageable = PageRequest.of(1, 5);


        Page<PostRequestListDTO> listDTOS = postHahTagFindService.searchPostsListByHashTag(pageable,hashTag);
        List<PostRequestListDTO> findPosts = listDTOS.get().collect(Collectors.toList());

        //then
        assertThat(findPosts).hasSize(5);
        assertThat(findPosts).have(new Condition<>(postRequestListDTO -> postRequestListDTO.getTitle().equals("test01"), "test01"));
    }
}