package com.geulkkoli.domain.admin.service;

import com.geulkkoli.domain.admin.AccountLock;
import com.geulkkoli.domain.admin.AccountLockRepository;
import com.geulkkoli.domain.admin.Report;
import com.geulkkoli.domain.admin.ReportRepository;
import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.posthashtag.PostHashTagRepository;
import com.geulkkoli.domain.topic.Topic;
import com.geulkkoli.domain.topic.TopicRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.admin.DailyTopicDto;
import com.geulkkoli.web.admin.ReportDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AdminServiceImplTest {
    @Autowired
    private PostHashTagRepository postHashTagRepository;
    @Autowired
    private AccountLockRepository accountLockRepository;

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private HashTagRepository hashTagRepository;

    private User user;
    private User user2;

    @BeforeEach
    void init() {
        User user = User.builder()
                .email("test121@gmail.com")
                .userName("name121")
                .nickName("nickName" + UUID.randomUUID())
                .phoneNo("010-1111-2222")
                .password("1234")
                .gender("Male")
                .build();
        this.user = userRepository.save(user);

        User user2 = User.builder()
                .email("test145@gmail.com")
                .userName("name121")
                .nickName("33231")
                .phoneNo("010-12111-2222")
                .password("1234")
                .gender("Male")
                .build();
        this.user2 = userRepository.save(user2);
        HashTag tag2 = hashTagRepository.save(new HashTag("공지글", HashTagType.MANAGEMENT));
        HashTag tag3 = hashTagRepository.save(new HashTag("판타지", HashTagType.CATEGORY));
        HashTag tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.CATEGORY));
        HashTag 완결 = hashTagRepository.save(new HashTag("완결", HashTagType.STATUS));
        HashTag 소설 = hashTagRepository.save(new HashTag("소설", HashTagType.CATEGORY));
    }


    @AfterEach
    void tearDown() {
        postHashTagRepository.deleteAllInBatch();
        accountLockRepository.deleteAllInBatch();
        hashTagRepository.deleteAllInBatch();
        reportRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        topicRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("게시글 작성자 조회")
    void findUserByPostId() {
        //given
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .nickName("점심뭐먹지").build();

        Post post = user.writePost(writeRequestDTO);
        postRepository.save(post);

        //when
        User userByPostId = adminService.findUserByPostId(post.getPostId());

        //then
        assertThat(user).isEqualTo(userByPostId);
    }

    @Test
    @DisplayName("신고받은 게시글 조회")
    void findReportedPosts() {
        //given
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .nickName("점심뭐먹지").build();

        Post post = user.writePost(writeRequestDTO);
        postRepository.save(post);


        WriteRequestDTO writeRequestDTO1 = WriteRequestDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName("점심뭐먹지").build();
        Post post1 = user.writePost(writeRequestDTO1);

        postRepository.save(post1);

        Report report = user2.writeReport(post, "욕설");
        Report report1 = user2.writeReport(post1, "비 협조적");

        reportRepository.save(report);
        reportRepository.save(report1);

        //when
        List<ReportDto> reportedPosts = adminService.findAllReportedPost();

        //then
        assertThat(reportedPosts).hasSize(2);
    }

    @Test
    @DisplayName("유저 잠금")
    void lockUser() {
        //given
        Long userId = user.getUserId();

        //when
        AccountLock accountLock1 = adminService.lockUser(userId, "욕설", 3L);

        //then
        assertThat(user.isLock()).isTrue();
        assertThat(accountLock1.getReason()).isEqualTo("욕설");
        assertThat(accountLock1.getLockExpirationDate()).has(new Condition<>(localDateTime -> localDateTime.isAfter(LocalDateTime.now()), "잠금시간"));
    }


    @Test
    @DisplayName("주제를 Dto로 잘 바꿨는지")
    void findWeeklyTopicTest() {
        //given
        for (int i = 0; i < 31; i++) {
            Topic topic = Topic.builder()
                    .topicName("testTopic" + i)
                    .build();
            topicRepository.save(topic);
        }


        List<Topic> topicsByUseDateBefore = topicRepository.findTopicsByUseDateBefore(LocalDate.now());

        //when
        List<DailyTopicDto> weeklyTopic = adminService.findWeeklyTopic();

        //then
        assertThat(weeklyTopic).hasSize(10);
    }

    @DisplayName("게시글을 삭제한다")
    @Test
    void deletePost() {
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .nickName("점심뭐먹지").build();

        Post post = user.writePost(writeRequestDTO);
        postRepository.save(post);

        //when
        adminService.deletePost(post.getPostId());

        //then
        assertThat(postRepository.findById(post.getPostId())).isEmpty();
    }

    @DisplayName("공지사항을 저장한다")
    @Test
    void saveNotice() {
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .tagList("이벤트 소설 완결")
                .nickName("점심뭐먹지").build();


        Post post = adminService.saveNotice(writeRequestDTO, user);

        assertAll(
                () -> assertThat(post.getTitle()).isEqualTo("testTitle"),
                () -> assertThat(post.getPostBody()).isEqualTo("test postbody"),
                () -> assertThat(post.getPostHashTags()).hasSize(3)
        );
    }

    @DisplayName("공지사항을 수정한다")
    @Test
    void updateNotice() {
        WriteRequestDTO writeRequestDTO = WriteRequestDTO.builder()
                .title("testTitle")
                .postBody("test postbody")
                .tagList("이벤트 완결")
                .nickName("점심뭐먹지")
                .build();

        Post post = adminService.saveNotice(writeRequestDTO, user);
        ArticleEditRequestDTO articleEditRequestDTO = ArticleEditRequestDTO.builder()
                .title("testTitle1")
                .postBody("test")
                .tags("이벤트 구독 소설")
                .nickName("밥뭐먹지")
                .build();
        Post editPost = adminService.updateNotice(post.getPostId(), articleEditRequestDTO);

        assertAll(
                () -> assertThat(editPost.getTitle()).isEqualTo("testTitle1"),
                () -> assertThat(editPost.getPostBody()).isEqualTo("test"),
                () -> assertThat(editPost.getPostHashTags()).hasSize(3));

    }

    @DisplayName("주제를 수정한다")
    @Test
    void updateTopic() {
        //given
        Topic topic = Topic.builder()
                .topicName("testTopic" + 1)
                .build();
        topicRepository.save(topic);


        DailyTopicDto dailyTopicDto = DailyTopicDto.builder()
                .topic("testTopic21")
                .date("2000-01-01")
                .build();
        //when
        Topic topic2 = adminService.updateTopic(dailyTopicDto);

        //then
        assertThat(topic2.getTopicName()).isEqualTo(dailyTopicDto.getTopic());
    }

}