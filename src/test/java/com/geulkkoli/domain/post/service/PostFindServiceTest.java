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
import com.geulkkoli.web.blog.dto.WriteRequestDTO;
import com.geulkkoli.web.blog.dto.ArticlePagingRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
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

        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("title")
                .tagList("#소설#완결")
                .postBody("body")
                .nickName("nick")
                .tagList("")
                .build();
        Post save = postService.writeArtice(writeRequestDTO, user1);

        Post post = postFindService.findById(save.getPostId());

        assertThat("title").isEqualTo(post.getTitle());
        assertThat("body").isEqualTo(post.getPostBody());
        assertThat("nick").isEqualTo(post.getNickName());
        assertThat("userName").isEqualTo(post.getUser().getUserName());
        assertThat("email@email.com").isEqualTo(post.getUser().getEmail());
        assertThat("phoneNo").isEqualTo(post.getUser().getPhoneNo());

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

        WriteRequestDTO writeRequestDTO1 = WriteRequestDTO.builder()
                .title("calender")
                .postBody("test")
                .nickName(testUser.getNickName())
                .build();

        Post writePost = testUser.writePost(writeRequestDTO1);

        WriteRequestDTO writeRequestDTO2 = WriteRequestDTO.builder()
                .title("calender")
                .postBody("test")
                .nickName(testUser.getNickName())
                .build();

        Post writePost1 = testUser.writePost(writeRequestDTO2);

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

        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("title")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build();
        WriteRequestDTO writeRequestDTO1 = WriteRequestDTO.builder()
                .title("title")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build();
        Post save = postService.writeArtice(writeRequestDTO, user1);

        Post save1 = postService.writeArtice(writeRequestDTO1, user1);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<ArticlePagingRequestDTO> postRequestListDTOS = postFindService.searchPostsList(pageable, SearchType.TITLE.getType(), "title");

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

        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("title")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build();
        WriteRequestDTO writeRequestDTO1 = WriteRequestDTO.builder()
                .title("title1")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build();
        WriteRequestDTO writeRequestDTO2 = WriteRequestDTO.builder()
                .title("fsdfsdf")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build();
        WriteRequestDTO writeRequestDTO3 = WriteRequestDTO.builder()
                .title("sdfsdfds")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build();


        Post save = postService.writeArtice(writeRequestDTO, user1);
        Post save1 = postService.writeArtice(writeRequestDTO1, user1);
        Post save2 = postService.writeArtice(writeRequestDTO2, user1);
        Post save3 = postService.writeArtice(writeRequestDTO3, user1);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<ArticlePagingRequestDTO> postRequestListDTOS = postFindService.searchPostsList(pageable, "제목", "title fsdfsdf sdfsdfds");

        //then
        assertAll(
                () -> assertThat(postRequestListDTOS).hasSize(4),
                () -> assertThat(postRequestListDTOS).extracting("title").contains("title", "title1", "fsdfsdf", "sdfsdfds"));
    }

    @DisplayName("다양한 검색어와 검색 타입으로 검색하기")
    @ParameterizedTest
    @CsvSource(value = {"제목, title fsdfsdf sdfsdfds", "닉네임, nick", "내용, body", "멀티 해시태그, 소설 완결"}, delimiter = ',')
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

        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("title")
                .tagList("#소설#완결")
                .postBody("body")
                .nickName("nick")
                .build();
        WriteRequestDTO writeRequestDTO1 = writeRequestDTO.builder()
                .title("ㅁㄷ")
                .tagList("#testTag")
                .postBody("ㅇㄹㅁ")
                .nickName("ㅁㄹ")
                .build();

        WriteRequestDTO writeRequestDTO2 = writeRequestDTO.builder()
                .title("fsdfsdf")
                .tagList("#testTag#소설#완결")
                .postBody("body sdfsd")
                .nickName("nick")
                .build();

        WriteRequestDTO writeRequestDTO3 = writeRequestDTO.builder()
                .title("sdfsdfds")
                .tagList("#testTag#소설#완결")
                .postBody("body")
                .nickName("nick")
                .build();


        Post save = postService.writeArtice(writeRequestDTO, user1);
        Post save1 = postService.writeArtice(writeRequestDTO1, user1);
        Post save2 = postService.writeArtice(writeRequestDTO2, user1);
        Post save3 = postService.writeArtice(writeRequestDTO3, user1);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<ArticlePagingRequestDTO> postRequestListDTOS = postFindService.searchPostsList(pageable, searchType, searchWords);

        assertAll(
                () -> assertThat(postRequestListDTOS).hasSize(3),
                () -> assertThat(postRequestListDTOS).extracting("title").contains("title", "fsdfsdf", "sdfsdfds"));
    }

    @DisplayName("멀티 해시태그로 검색하기")
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
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("test Title1")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .authorId(user1.getUserId())
                .build();
        WriteRequestDTO writeRequestDTO1 = WriteRequestDTO.builder()
                .title("test Title2")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .authorId(user1.getUserId())
                .build();
        WriteRequestDTO writeRequestDTO2 = WriteRequestDTO.builder()
                .title("test Title3")
                .tagList("#testTag#소설#완결")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .authorId(user1.getUserId())
                .build();


        Post save = postService.writeArtice(writeRequestDTO, user1);
        Post save1 = postService.writeArtice(writeRequestDTO1, user1);
        Post save2 = postService.writeArtice(writeRequestDTO2, user1);
        Pageable pageable = PageRequest.of(0, 5);
        HashTag 소설 = hashTagRepository.findByHashTagName("소설");
        HashTag 완결 = hashTagRepository.findByHashTagName("완결");

        //when
        Page<ArticlePagingRequestDTO> postRequestListDTOS = postFindService.findPostByTag(pageable, List.of(소설, 완결));

        //then
        assertThat(postRequestListDTOS).hasSize(3);
        assertThat(postRequestListDTOS).extracting("title").contains("test Title1", "test Title2", "test Title3");
    }
}