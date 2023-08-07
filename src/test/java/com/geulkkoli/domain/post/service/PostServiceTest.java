package com.geulkkoli.domain.post.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.NotAuthorException;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostNotExistException;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.post.service.PostFindService;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserNotExistException;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.EditDTO;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * TestDataInit Class 참조
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostFindService postFindService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostHashTagRepository postHashTagRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HashTagRepository hashTagRepository;

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

    @DisplayName("게시글 저장")
    @Test
    void savePost() {
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName")
                .build();

        userRepository.save(user1);

        AddDTO addDTO = AddDTO.builder()
                .title("testTitle")
                .tagList("testTag testTag2, testTag3")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build();
        Post save = postService.savePost(addDTO, user1);


        assertAll(() -> assertThat(save).has(new Condition<>(p -> p.getTitle().equals("title"), "title")),
                () -> assertThat(save).has(new Condition<>(p -> p.getPostBody().equals("body"), "body")),
                () -> assertThat(save).has(new Condition<>(p -> p.getNickName().equals("nick"), "nick")),
                () -> assertThat(save.getPostHashTags()).hasSize(3));

    }


    @DisplayName("게시글 수정")
    @Test
    void updatePost() {
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName")
                .build();

        userRepository.save(user1);


        Post post = postService.savePost(AddDTO.builder().authorId(user1.getUserId()).postBody("body").title("title").tagList("소설 판타지 완결").nickName(user1.getNickName()).build(), user1);
        EditDTO editDTO = EditDTO.builder()
                .postId(post.getPostId())
                .postBody("body update")
                .title("title update")
                .tags("판타지 완결")
                .nickName("nick update")
                .build();
        postService.updatePost(post, editDTO);

        Post one = postFindService.findById(post.getPostId());


        assertAll(() -> assertThat(one.getTitle()).isEqualTo("title update"),
                () -> assertThat(one.getPostBody()).isEqualTo("body update"),
                () -> assertThat(one.getNickName()).isEqualTo("nick update"),
                () -> assertThat(one.getPostHashTags()).hasSize(2),
                () -> assertThat(one.getPostHashTags().get(0).getHashTag().getHashTagName()).isEqualTo("판타지"),
                () -> assertThat(one.getPostHashTags().get(1).getHashTag().getHashTagName()).isEqualTo("완결"));
    }

    @DisplayName("게시글 삭제")
    @Test
    void deletePost() {
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName")
                .build();

        userRepository.save(user1);

        Post post = postService.savePost( AddDTO.builder()
                .title("testTitle")
                .tagList("testTag testTag2")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build(), user1);

        postService.deletePost(post.getPostId(), user1.getUserId());

        assertThrows(PostNotExistException.class, () -> postFindService.findById(post.getPostId()));
    }

    @Test
    void showDetailPost() {
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName")
                .build();

        User save = userRepository.save(user1);

        AddDTO addDTO = AddDTO.builder()
                .title("testTitle")
                .tagList("testTag testTag2")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build();

        Post post = postService.savePost(addDTO, user1);

        Post findPost = postService.showDetailPost(post.getPostId());

        assertAll(() -> assertThat(findPost.getTitle()).isEqualTo("testTitle"),
                () -> assertThat(findPost.getPostBody()).isEqualTo("test postbody"),
                () -> assertThat(findPost.getNickName()).isEqualTo("점심뭐먹지"));

    }

    @DisplayName("게시글을 삭제시 존재하지 않는 게시글 아이디 번호로 게시글을 삭제하려고 하면 예외가 발생한다.")
    @Test
    void deletePostException() {
        assertThatThrownBy(() -> postService.deletePost(100L, 1L))
                .isInstanceOf(PostNotExistException.class)
                .hasMessage("해당 아이디를 가진 게시글이 존재하지 않습니다: 100");
    }

    @DisplayName("로그인한 회원 아이디로 회원을 찾지 못하면 예외가 발생한다.")
    @Test
    void deletePostException2() {
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName")
                .build();

        userRepository.save(user1);
        Post post = postService.savePost( AddDTO.builder()
                .title("testTitle")
                .tagList("testTag testTag2")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build(), user1);

        assertThatThrownBy(() -> postService.deletePost(post.getPostId(), 2L))
                .isInstanceOf(UserNotExistException.class)
                .hasMessage("일치하는 회원을 찾을 수 없습니다: 2");
    }

    @DisplayName("게시글을 삭제시 게시글 아이디와 로그인한 회원 아이디가 일치하지 않으면 예외가 발생한다.")
    @Test
    void deletePostException3() {
        User user1 = User.builder()
                .email("email@email.com")
                .userName("userName")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo")
                .nickName("nickName")
                .build();

        User user2 = User.builder()
                .email("email2@email.com")
                .userName("userName2")
                .gender("gender")
                .password("password")
                .phoneNo("phoneNo2")
                .nickName("nickName2")
                .build();


        userRepository.save(user1);
        userRepository.save(user2);
        Post post = postService.savePost( AddDTO.builder()
                .title("testTitle")
                .tagList("testTag testTag2")
                .postBody("test postbody")
                .nickName("점심뭐먹지")
                .tagList("")
                .build(), user1);

        assertThatThrownBy(() -> postService.deletePost(post.getPostId(), user2.getUserId()))
                .isInstanceOf(NotAuthorException.class)
                .hasMessage("해당 게시글의 작성자가 아닙니다.");

    }
}