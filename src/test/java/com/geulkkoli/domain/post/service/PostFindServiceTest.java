package com.geulkkoli.domain.post.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.post.SearchType;
import com.geulkkoli.domain.posthashtag.PostHashTag;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.PostRequestListDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PostFindServiceTest {

    @Autowired
    PostFindService postFindService;
    @Autowired
    PostService postService;
    @Autowired
    HashTagRepository hashTagRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostHashTagRepository postHashTagRepository;

    @BeforeEach
    void setUp() {
        HashTag tag2 = hashTagRepository.save(new HashTag("공지글", HashTagType.MANAGEMENT));
        HashTag tag3 = hashTagRepository.save(new HashTag("판타지", HashTagType.CATEGORY));
        HashTag tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.CATEGORY));
        HashTag 완결 = hashTagRepository.save(new HashTag("완결", HashTagType.STATUS));
        HashTag 소설 = hashTagRepository.save(new HashTag("소설", HashTagType.CATEGORY));
    }

    @AfterEach
    void tearDown() {
        postHashTagRepository.deleteAllInBatch();
        hashTagRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @DisplayName("게시글 번호로 조회")
    @Test
    void findById() {

        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName12")
                .build();

        userRepository.save(user1);

        AddDTO addDTO = new AddDTO(1L, "title", "body", "nick", "testTag", "소설", "완결");
        Post save = postService.savePost(addDTO, user1);

        Post post = postFindService.findById(save.getPostId());
        List<PostHashTag> postHashTags = post.getPostHashTags();

        assertThat("title").isEqualTo(post.getTitle());
        assertThat("body").isEqualTo(post.getPostBody());
        assertThat("nick").isEqualTo(post.getNickName());
        assertThat(postHashTags).extracting("hashTag").extracting("hashTagName").contains("완결");
        assertThat(postHashTags).extracting("hashTag").extracting("hashTagName").contains("소설");
    }

    @DisplayName("게시글 날짜를 조히")
    @Test
    void getCreatedAts() {
        User user = User.builder()
                .email("created@ats.com")
                .userName("test")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName123")
                .build();

        User testUser = userRepository.save(user);

        AddDTO addDTO1 = AddDTO.builder()
                .title("calender")
                .postBody("test")
                .nickName(testUser.getNickName())
                .build();

        Post writePost = testUser.writePost(addDTO1);

        AddDTO addDTO2 = AddDTO.builder()
                .title("calender")
                .postBody("test")
                .nickName(testUser.getNickName())
                .build();

        Post writePost1 = testUser.writePost(addDTO2);

        List<String> createdAts = postFindService.getCreatedAts(testUser);

        assertAll(
                () -> assertThat(createdAts).hasSize(2),
                () -> assertThat(createdAts).contains(writePost.getCreatedAt(), writePost1.getCreatedAt())
        );
    }

    @Test
    @DisplayName("게시글 제목으로 검색하기")
    void searchPostsListByTitle() {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName12")
                .build();

        userRepository.save(user1);

        AddDTO addDTO = new AddDTO(1L, "title", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO1 = new AddDTO(1L, "title1", "body", "nick", "testTag", "소설", "완결");
        Post save = postService.savePost(addDTO, user1);

        Post save1 = postService.savePost(addDTO1, user1);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<PostRequestListDTO> postRequestListDTOS = postFindService.searchPostsList(pageable, SearchType.TITLE.getType(), "title");

        //then
        assertAll(
                () -> assertThat(postRequestListDTOS).hasSize(2),
                () -> assertThat(postRequestListDTOS).extracting("title").contains("title"));
    }

    @Test
    @DisplayName("게시글 제목으로 검색하기 공백이 포함되어 있을 때")
    void searchPostsListByTitle_contain_white_space() {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName12")
                .build();

        userRepository.save(user1);

        AddDTO addDTO = new AddDTO(1L, "title", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO1 = new AddDTO(1L, "title1", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO2 = new AddDTO(1L, "fsdfsdf", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO3 = new AddDTO(1L, "sdfsdfds", "body", "nick", "testTag", "소설", "완결");


        Post save = postService.savePost(addDTO, user1);
        Post save1 = postService.savePost(addDTO1, user1);
        Post save2 = postService.savePost(addDTO2, user1);
        Post save3 = postService.savePost(addDTO3, user1);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<PostRequestListDTO> postRequestListDTOS = postFindService.searchPostsList(pageable, "제목", "title fsdfsdf sdfsdfds");

        //then
        assertAll(
                () -> assertThat(postRequestListDTOS).hasSize(4),
                () -> assertThat(postRequestListDTOS).extracting("title").contains("title", "title1", "fsdfsdf", "sdfsdfds"));
    }

    @DisplayName("다양한 검색어와 검색 타입으로 검색하기")
    @ParameterizedTest
    @CsvSource(value = {"제목, title fsdfsdf sdfsdfds", "닉네임, nick", "내용, body","멀티 해시태그, 소설#완결"}, delimiter = ',')
    void searchPostsList(String searchType, String searchWords) {
        //given
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName12")
                .build();

        userRepository.save(user1);

        AddDTO addDTO = new AddDTO(1L, "title", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO1 = new AddDTO(1L, "ㅁㄷ", "ㅇㄹㅁ", "ㅁㄹ", "testTag", "소설", "연재");
        AddDTO addDTO2 = new AddDTO(1L, "fsdfsdf", "body sdfsd", "nick", "testTag", "소설", "완결");
        AddDTO addDTO3 = new AddDTO(1L, "sdfsdfds", "body", "nick", "testTag", "소설", "완결");


        Post save = postService.savePost(addDTO, user1);
        Post save1 = postService.savePost(addDTO1, user1);
        Post save2 = postService.savePost(addDTO2, user1);
        Post save3 = postService.savePost(addDTO3, user1);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<PostRequestListDTO> postRequestListDTOS = postFindService.searchPostsList(pageable, searchType, searchWords);

        assertAll(
                () -> assertThat(postRequestListDTOS).hasSize(3),
                () -> assertThat(postRequestListDTOS).extracting("title").contains("title", "fsdfsdf", "sdfsdfds"));
    }

    @Test
    void findPostByTag() {
        ///given
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName12")
                .build();

        userRepository.save(user1);
        AddDTO addDTO = new AddDTO(1L, "title", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO1 = new AddDTO(1L, "title1", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO2 = new AddDTO(1L, "fsdfsdf", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO3 = new AddDTO(1L, "sdfsdfds", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO4 = new AddDTO(1L, "sdfsdfds", "body", "nick", "testTag", "소설", "완결");
        AddDTO addDTO5 = new AddDTO(1L, "sdfsdfds", "body", "nick", "testTag", "소설", "완결");
        Post save = postService.savePost(addDTO, user1);
        Post save1 = postService.savePost(addDTO1, user1);
        Post save2 = postService.savePost(addDTO2, user1);
        Post save3 = postService.savePost(addDTO3, user1);
        Post save4 = postService.savePost(addDTO4, user1);
        Post save5 = postService.savePost(addDTO5, user1);
        Pageable pageable = PageRequest.of(0, 5);
        HashTag 소설 = hashTagRepository.findByHashTagName("소설");
        HashTag 완결 = hashTagRepository.findByHashTagName("완결");

        //when
        Page<PostRequestListDTO> postRequestListDTOS = postFindService.findPostByTag(pageable,List.of(소설, 완결));

        //then
        assertThat(postRequestListDTOS).hasSize(5);
        assertThat(postRequestListDTOS).extracting("title").contains("title", "title1", "fsdfsdf", "sdfsdfds", "sdfsdfds");
    }
}